package com.justdoit.keller.common.util;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.config.RequestConfig;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数验证工具类
 * @author yangkaile
 * @date 2019-10-11 16:19:26
 */
public class RequestUtil {

    /**
     * 记录请求成功的日志（使用json提交参数的请求）
     * @param request 请求
     * @param params 请求参数
     * @param response 应答
     */
    public static void successLog(HttpServletRequest request, Map<String,String> params, ResponseEntity response){
        String requestId = StringUtils.getUUID();
        Console.info("api header",requestId,getHeader(request));
        Console.info("param",requestId,params);
        Console.info("response success",requestId,response.getBody());
    }

    /**
     * 记录请求成功的日志（使用form提交参数的请求，请求在Request中）
     * @param request 请求
     * @param response 应答
     */
    public static void successLog(HttpServletRequest request, ResponseEntity response){
        String requestId = StringUtils.getUUID();
        Console.info("form header",requestId,getHeader(request));
        Console.info("param",requestId,getParam(request));
        Console.info("response success",requestId,response.getBody());
    }

    /**
     * 记录请求失败的日志（使用json提交参数的请求）
     * @param request 请求
     * @param params 参数
     * @param response 应答
     */
    public static void errorLog(HttpServletRequest request, Map<String,String> params, ResponseEntity response){
        String requestId = StringUtils.getUUID();
        Console.error("api header",requestId,getHeader(request));
        Console.error("param",requestId,params);
        Console.error("response error",requestId,response.getBody());
    }

    /**
     * 记录请求失败的日志（使用form提交参数的请求，请求在Request中）
     * @param request 请求
     * @param response 应答
     */
    public static void errorLog(HttpServletRequest request, ResponseEntity response){
        String requestId = StringUtils.getUUID();
        Console.error("form header",requestId,getHeader(request));
        Console.error("param",requestId,getParam(request));
        Console.error("response error",requestId,response.getBody());
    }

    public static HashMap<String,String> getHeader(HttpServletRequest request){
        HashMap<String,String> headerMap = new HashMap<>(16);
        //请求的URL地址
        headerMap.put(RequestConfig.URL,request.getRequestURL().toString());
        //请求的资源
        headerMap.put(RequestConfig.URI,request.getRequestURI());
        //请求方式 GET/POST
        headerMap.put(RequestConfig.REQUEST_METHOD,request.getMethod());

        //来访者的IP地址
        headerMap.put(RequestConfig.REMOTE_ADDR,request.getRemoteAddr());
        //来访者的HOST
        headerMap.put(RequestConfig.REMOTE_HOST,request.getRemoteHost());
        //来访者的端口
        headerMap.put(RequestConfig.REMOTE_PORT,request.getRemotePort() + "");
        //来访者的用户名
        headerMap.put(RequestConfig.REMOTE_USER,request.getRemoteUser());


        //自定义的Header （接口名）
        headerMap.put(RequestConfig.METHOD,request.getHeader(RequestConfig.METHOD));
        //自定义的Header （TOKEN）
        headerMap.put(RequestConfig.TOKEN,request.getHeader(RequestConfig.TOKEN));
        return headerMap;
    }

    public static Map<String,String> getParam(HttpServletRequest request){
        Map<String,String> paramMap = new HashMap<>(16);
        //request对象封装的参数是以Map的形式存储的
        Map<String, String[]> map = request.getParameterMap();
        for(Map.Entry<String, String[]> entry :map.entrySet()){
            String paramName = entry.getKey();
            String paramValue = "";
            String[] paramValueArr = entry.getValue();
            for (int i = 0; paramValueArr!=null && i < paramValueArr.length; i++) {
                if (i == paramValueArr.length-1) {
                    paramValue += paramValueArr[i];
                }else {
                    paramValue += paramValueArr[i]+",";
                }
            }
            paramMap.put(paramName,paramValue);
        }
        if(paramMap.size() == 0){
            return null;
        }
        return paramMap;
    }
    public static String getUrl(Map<String,String> params,HttpServletRequest request){
        HashMap<String,String> headers = getHeader(request);
        StringBuilder builder = new StringBuilder();

        builder
                .append(PublicConstant.address).append(":")
                .append(PublicConstant.port).append("/")
                .append(headers.get(RequestConfig.METHOD));
        if(params == null){
            return builder.toString();
        }
        builder.append("?");
        for(String key :params.keySet()){
            builder.append(key)
                    .append("={")
                    .append(key)
                    .append("}&");
        }
        Console.info(builder.toString());
        return builder.toString();
    }

}
