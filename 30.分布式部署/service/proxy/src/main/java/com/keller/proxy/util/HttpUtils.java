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

    private static String getServiceUrl(LoadBalancerClient loadBalancer,HttpServletRequest request){
        String service = request.getHeader(RequestUtil.SERVICE);
        if(ObjectUtils.isEmpty(service)){
            return null;
        }
        String serveUrl = ServerRouter.getServeUrl(loadBalancer,service);
        if(ObjectUtils.isEmpty(serveUrl)){
            return null;
        }
        return serveUrl;
    }

    public static ResponseEntity<String> sendHttp(
            LoadBalancerClient loadBalancer,
            RabbitTemplate rabbitTemplate,
            HttpServletRequest request,
            String mapping){
        ResponseEntity<String> responseEntity ;
        Map<String,Object> params = RequestUtil.getParam(request);

        String url = getServiceUrl(loadBalancer,request);

        if(Constants.baseMapping.equals(mapping)){
            url += RequestUtil.getBaseUrl(request,params);
        }else if(Constants.adminMapping.equals(mapping)){
            url += RequestUtil.getAdminUrl(request,params);
        }else {
            url += RequestUtil.getUrl(request,params);
        }
        RestTemplate restTemplate = RestTemplateUtils.getInstance();
        try {
            if(RequestUtil.GET.equals(request.getMethod())){
                if(params == null){
                    responseEntity = restTemplate.getForEntity(url, String.class);
                }else {
                    responseEntity = restTemplate.getForEntity(url, String.class, params);
                }
            }else {
                responseEntity = restTemplate.postForEntity(url,null,String.class,params);
            }
        }catch (Exception e){
            responseEntity = ResponseUtils.getResponseFromException(e);
        }
        ErrorLogUtils.getInstance(rabbitTemplate).requestLog(RequestUtil.getRealIp(request),request.getMethod(),url,params,responseEntity);
        return responseEntity;
    }
}
