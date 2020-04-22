package com.justdoit.keller.common.util;

import com.alibaba.fastjson.JSONObject;
import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.config.RequestConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求处理工具类，记录请求日志、解析请求参数等
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
    public static void successLog(HttpServletRequest request, Map<String,Object> params, ResponseEntity response){
        Console.info("response success",getHeader(request),params,response.getBody());
    }

    /**
     * 记录请求失败的日志（使用json提交参数的请求）
     * @param request 请求
     * @param params 参数
     * @param response 应答
     */
    private static void errorLog(HttpServletRequest request, Map<String,Object> params, ResponseEntity response){
        Console.error("response success",getHeader(request),params,response.getBody());
    }


    private static HashMap<String,String> getHeader(HttpServletRequest request){
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

    /**
     *  读取 Body 中的参数，并将其转换成 Map
     * @param request
     * @return
     */
    public static Map<String,Object> getBodyParams(HttpServletRequest request){
        BufferedReader streamReader = null;
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        try {
            streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            String str = responseStrBuilder.toString();
            if(str.isEmpty()){
                return null;
            }
            JSONObject jsonObject = JSONObject.parseObject(str);
            Map<String,Object> params = jsonObject.getInnerMap();
            if(params == null || params.size() < 1){
                return null;
            }
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try{
                streamReader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static Map<String,Object> getParam(HttpServletRequest request){
        Map<String,Object> paramMap = new HashMap<>(16);
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

    /**
     * 根据请求参数获取到URL，会解析请求中的 JWT，并将解析出的 UserId 拼接在请求中
     * @param request
     * @return
     */
    private static String getUrl(Map<String,Object> params,HttpServletRequest request){
        //从 JWT 中解析出 UserId
        Integer userId = JwtUtils.getUserIdFromLogin(request.getHeader(RequestConfig.TOKEN));
        HashMap<String,String> headers = getHeader(request);
        StringBuilder builder = new StringBuilder();

        builder.append(PublicConstant.serviceUrl).append("/")
                .append(headers.get(RequestConfig.METHOD));
        builder.append("?");
        //请求参数中添加 userId
        builder.append(PublicConstant.USER_ID_KEY).append("=").append(userId).append("&");
        if(params == null){
            return builder.toString();
        }
        for(String key :params.keySet()){
            //过滤掉请求中的 userId
            if(PublicConstant.USER_ID_KEY.equals(key)){
                continue;
            }
            builder.append(key)
                    .append("={")
                    .append(key)
                    .append("}&");
        }
        Console.info(builder.toString());
        return builder.toString();
    }

    public static ResponseEntity doGet(HttpServletRequest request,Map<String,Object> params,RestTemplate restTemplate){
        ResponseEntity responseEntity ;
        try {
            String url = getUrl(params,request);
            if(params == null){
                responseEntity = restTemplate.getForEntity(url, String.class);
            }else{
                responseEntity = restTemplate.getForEntity(url, String.class,params);
            }
            successLog(request,params,responseEntity);
        }catch (Exception e){
            responseEntity = ResponseUtils.getResponseFromException(e);
            errorLog(request,params,responseEntity);
        }
        return responseEntity;
    }



    public static ResponseEntity doPost(HttpServletRequest request,Map<String,Object> params,RestTemplate restTemplate){
        ResponseEntity responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(getUrl(params,request),null,String.class,params);
            successLog(request,params,responseEntity);
        }catch (Exception e){
            responseEntity = ResponseUtils.getResponseFromException(e);
            errorLog(request,params,responseEntity);
        }
        return responseEntity;
    }

}
