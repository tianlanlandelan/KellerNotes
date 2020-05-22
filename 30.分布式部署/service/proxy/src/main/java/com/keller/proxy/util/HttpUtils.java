package com.keller.proxy.util;

import com.keller.common.config.Constants;
import com.keller.common.http.RequestUtil;
import com.keller.common.http.ResponseUtils;
import com.keller.common.http.RestTemplateUtils;
import com.keller.common.http.ServerRouter;
import com.keller.common.util.ErrorLogUtils;
import com.keller.common.util.ObjectUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;



/**
 * 请求处理工具类，记录请求日志、解析请求参数等
 * @author yangkaile
 * @date 2019-10-11 16:19:26
 */
public class HttpUtils {

    /**
     *  获取服务的访问地址
     *      解析请求中的 service Header，获取到访问的服务名
     *      从负载均衡器中根据服务名获取到一个服务的 url
     * @param loadBalancer  负载均衡器
     * @param request       原始请求
     * @return
     */
    private static String getServiceUrl(LoadBalancerClient loadBalancer,HttpServletRequest request){
        // 从 request 中获取到请求的服务名
        String service = request.getHeader(RequestUtil.SERVICE);
        // 因为要将请求转发到相应的服务，在这里不允许请求不带服务名
        if(ObjectUtils.isEmpty(service)){
            return null;
        }
        // 从 loadBalancer 中获取到一个可用的服务地址并返回，可能获取不到，也就是 null，也返回，由调用方处理
        return ServerRouter.getServeUrl(loadBalancer,service);
    }

    /**
     * http 请求发送工具类
     * 根据 request 解析出请求的服务器地址、请求参数、请求方式
     * 根据 mapping 构建不同的请求 URL
     * 按照不同的请求方式转发原始请求到实际的访问地址，并返回应答
     * 将异常请求发送到 RabbitMQ，由 Log 服务负责日志记录
     * @param loadBalancer      负载均衡器
     * @param rabbitTemplate    RabbitMQ 消息模板
     * @param request           原始请求
     * @param mapping           请求的固定Mapping，如果带固定 Mapping，在访问接口时限定为固定Mapping
     * @return
     */
    public static ResponseEntity<String> sendHttp(
            LoadBalancerClient loadBalancer,
            RabbitTemplate rabbitTemplate,
            HttpServletRequest request,
            String mapping){
        // 定义返回数据
        ResponseEntity<String> responseEntity ;
        // 从请求中读取参数
        Map<String,Object> params = RequestUtil.getParam(request);

        // 获取请求的服务地址
        String url = getServiceUrl(loadBalancer,request);

        // 如果请求的是固定的 Mapping,获取固定的请求 URL
        if(Constants.baseMapping.equals(mapping)){
            // 获取无需权限控制的请求 URL
            url += RequestUtil.getBaseUrl(request,params);
        }else if(Constants.adminMapping.equals(mapping)){
            // 获取管理员请求 URL
            url += RequestUtil.getAdminUrl(request,params);
        }else {
            // 获取普通用户请求 URL
            url += RequestUtil.getUrl(request,params);
        }
        // 获取统一的 HTTP 请求模板
        RestTemplate restTemplate = RestTemplateUtils.getInstance();
        try {
            // 发送 GET 请求
            if(RequestUtil.GET.equals(request.getMethod())){
                if(params == null){
                    responseEntity = restTemplate.getForEntity(url, String.class);
                }else {
                    responseEntity = restTemplate.getForEntity(url, String.class, params);
                }
            }else {
                // 发送 POST 请求
                responseEntity = restTemplate.postForEntity(url,null,String.class,params);
            }
        }catch (Exception e){
            // 处理请求中的异常
            responseEntity = ResponseUtils.getResponseFromException(e);
        }
        // 记录请求日志
        ErrorLogUtils.getInstance(rabbitTemplate).requestLog(RequestUtil.getRealIp(request),request.getMethod(),url,params,responseEntity);
        // 返回请求结果
        return responseEntity;
    }
}
