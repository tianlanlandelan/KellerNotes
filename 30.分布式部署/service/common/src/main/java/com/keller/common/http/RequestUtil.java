package com.keller.common.http;

import com.alibaba.fastjson.JSONObject;
import com.keller.common.config.Constants;
import com.keller.common.util.Console;
import com.keller.common.util.JwtUtils;
import com.keller.common.util.ObjectUtils;

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
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String URL = "URL";
    public static final String URI = "URI";
    public static final String REMOTE_ADDR = "REMOTE_ADDR";
    public static final String REMOTE_HOST = "REMOTE_HOST";
    public static final String REMOTE_PORT = "REMOTE_PORT";
    public static final String REMOTE_USER = "REMOTE_USER";
    public static final String REQUEST_METHOD = "REQUEST_METHOD";
    /**
     * 自定义的服务名，用于微服务架构中的路由
     */
    public static final String SERVICE = "service";
    /**
     * 自定义的方法名
     */
    public static final String METHOD = "method";
    public static final String TOKEN = "token";

    private static HashMap<String,String> getHeader(HttpServletRequest request){
        HashMap<String,String> headerMap = new HashMap<>(16);
        //请求的URL地址
        headerMap.put(URL,request.getRequestURL().toString());
        //请求的资源
        headerMap.put(URI,request.getRequestURI());
        //请求方式 GET/POST
        headerMap.put(REQUEST_METHOD,request.getMethod());

        //来访者的IP地址
        headerMap.put(REMOTE_ADDR,request.getRemoteAddr());
        //来访者的HOST
        headerMap.put(REMOTE_HOST,request.getRemoteHost());
        //来访者的端口
        headerMap.put(REMOTE_PORT,request.getRemotePort() + "");
        //来访者的用户名
        headerMap.put(REMOTE_USER,request.getRemoteUser());


        //自定义的Header （接口名）
        headerMap.put(SERVICE,request.getHeader(SERVICE));
        //自定义的Header （接口名）
        headerMap.put(METHOD,request.getHeader(METHOD));
        //自定义的Header （TOKEN）
        headerMap.put(TOKEN,request.getHeader(TOKEN));
        return headerMap;
    }

    /**
     *  读取 Body 中的参数，并将其转换成 Map
     * @param request
     * @return
     */
    private static Map<String,Object> getBodyParams(HttpServletRequest request){
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


    /**
     * 先读取请求参数，再读取Body中的参数
     * @param request
     * @return
     */
    public static Map<String,Object> getParam(HttpServletRequest request){
        Map<String,Object> paramMap = getBodyParams(request);
        if(paramMap == null){
            paramMap = new HashMap<>(16);
        }
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
     * 访问普通接口
     * @param request
     * @param params
     * @return
     */
    public static String getUrl(HttpServletRequest request,Map<String,Object> params){
        return getUrl(request, params,false);
    }

    /**
     * 访问管理员接口（/admin）
     * @param request
     * @param params
     * @return
     */
    public static String getAdminUrl(HttpServletRequest request,Map<String,Object> params){
        return getUrl(request, params,true);
    }

    /**
     * 访问无需身份认证的接口 (/base)
     * @param request
     * @param params
     * @return
     */
    public static String getBaseUrl(HttpServletRequest request, Map<String,Object> params){
        HashMap<String,String> headers = getHeader(request);
        StringBuilder builder = new StringBuilder();

        builder.append(Constants.baseMapping).append("/")
                .append(headers.get(METHOD));
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
        String url = builder.substring(0,builder.length() -1);
        Console.info("getBaseUrl",url);
        return url;
    }

    /**
     *
     * @param request   请求
     * @param params    参数  不同过 request 获取参数是因为，在调用一次 request.getHeader后，stream就会被关闭，无法再读取到Body中的参数
     * @param isAdmin   要访问的是不是管理员接口
     * @return
     */
    private static String getUrl(HttpServletRequest request,Map<String,Object> params,boolean isAdmin){
        //从 JWT 中解析出 UserId
        HashMap<String,String> jwtMap= JwtUtils.readJwt(request.getHeader(TOKEN));

        // 从 JWT 中取出用户类型
        Integer userType = Integer.parseInt(jwtMap.get(JwtUtils.USER_TYPE));
        // 从 JWT 中取出用户ID
        Integer userId = Integer.parseInt(jwtMap.get(JwtUtils.ID));
        // 添加固定的用户 ID,避免客户端通过伪造 ID 参数对接口进行非法访问
        String userIdParamName;
        String mapping = "";
        // 用户类型和用户Id均不能为空
        if(ObjectUtils.isEmpty(userId,userType)){
            return null;
        }
        // 非管理员用户不能访问管理员接口
        if(isAdmin){
            if(userType != Constants.ADMIN_USER_TYPE){
                return null;
            }
            userIdParamName = Constants.ADMIN_ID_KEY;
            mapping = Constants.adminMapping;
        }else {
            userIdParamName = Constants.USER_ID_KEY;
        }
        StringBuilder builder = new StringBuilder();
        HashMap<String,String> headers = getHeader(request);

        builder.append(mapping)
                .append("/")
                .append(headers.get(METHOD))
                .append("?")
                .append(userIdParamName)
                .append("=")
                .append(userId);
        if(params == null){
            return builder.toString();
        }else{
            builder.append("&");
        }
        for(String key :params.keySet()){
            //过滤掉请求中的 userId
            if(Constants.USER_ID_KEY.equals(key)
                    || Constants.ADMIN_ID_KEY.equals(key)){
                continue;
            }
            builder.append(key)
                    .append("={")
                    .append(key)
                    .append("}&");
        }
        String url = builder.substring(0,builder.length() -1);
        Console.info("getUrl",url,isAdmin);
        return url;
    }


    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!isIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (!isIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (!isIP(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    private static boolean isIP(String ip){
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        return true;
    }
}
