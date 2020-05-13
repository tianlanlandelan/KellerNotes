package com.justdoit.keller.common.util;

import com.alibaba.fastjson.JSON;
import com.justdoit.keller.KellerApplication;
import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.entity.ErrorLog;
import com.justdoit.keller.mapper.ErrorLogMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Component
public class ErrorLogUtils {
    @Resource
    private ErrorLogMapper mapper;

    private static ErrorLogUtils utils;

    /**
     * 请求处理异常
     */
    public static final String REQUEST_ERROR = "Request";
    /**
     * 系统异常错误
     */
    public static final String SYSTEM_ERROR = "System";
    /**
     * 非法访问
     */
    public static final String ACCESS_ERROR = "Access";

    @PostConstruct
    public void init() {
        utils = this;
        utils.mapper = this.mapper;
    }



    /**
     * 记录请求成功的日志
     * @param ip
     * @param method
     * @param url
     * @param params
     * @param response
     */
    public static void requestSuccessLog(String ip, String method, String url, Map<String,Object> params, ResponseEntity response){
        ResultData resultData;
        if(response.getBody() instanceof  ResultData){
            resultData = (ResultData)response.getBody();
        }else{
            resultData = JSON.parseObject(response.getBody().toString(),ResultData.class);
        }
        if(resultData == null || resultData.getSuccess() == 0){
            Console.info("response requestSuccessLog",ip,method,url,params,response.getBody());
            return ;
        }
        requestErrorLog(ip, method, url, params, response);
    }

    /**
     * 记录错误请求日志
     *
     * @param ip
     * @param method
     * @param url
     * @param params
     * @param response
     */
    public static void requestErrorLog(String ip, String method, String url, Map<String,Object> params, ResponseEntity response){
        Console.error("response requestErrorLog",ip,method,url,params,response.getBody());
        ResultData resultData;
        if(response.getBody() instanceof  ResultData){
            resultData = (ResultData)response.getBody();
        }else{
            resultData = JSON.parseObject(response.getBody().toString(),ResultData.class);
        }
        ErrorLog log = new ErrorLog();
        log.setType(REQUEST_ERROR);
        log.setIp(ip);
        log.setMethod(method);
        log.setUrl(url);
        StringBuilder builder = new StringBuilder();
        for(String key : params.keySet()){
            builder.append(key).append(":").append(params.get(key)).append(",");
        }
        log.setParams(builder.toString());
        if(resultData != null){
            log.setMessage(resultData.getMessage());
        }else {
            log.setMessage(response.getBody().toString());
        }
        utils.mapper.baseInsertAndReturnKey(log);
    }

    /**
     * 记录系统错误日志
     * 1. 打印错误堆栈
     * 2. 异常日志入库
     *      只记录项目包中的异常堆栈，不记录JVM 的异常堆栈
     *
     * @param e
     */
    public static void errorLog(Exception e){
        e.printStackTrace();
        ErrorLog log = new ErrorLog(SYSTEM_ERROR);
        // 获取项目包名，只记录项目代码中的异常信息，不记录JVM的异常
        String packageName = KellerApplication.class.getPackage().getName();
        // 遍历异常
        StackTraceElement[] elements = e.getStackTrace();
        StringBuilder builder = new StringBuilder();

        builder.append(e.toString()).append(":");
        for(StackTraceElement element:elements){
            if(element.toString().startsWith(packageName)){
                builder.append(element.toString()).append(",");
            }
        }
        log.setMessage(builder.toString());
        utils.mapper.baseInsertAndReturnKey(log);
    }

    /**
     *
     * @param request
     */
    public static void accessErrorLog(HttpServletRequest request){
        ErrorLog log = new ErrorLog(ACCESS_ERROR);
        log.setIp(RequestUtil.getRealIp(request));
        log.setUrl(request.getRequestURI());
        log.setMethod(request.getMethod());
        utils.mapper.baseInsertAndReturnKey(log);
    }

}
