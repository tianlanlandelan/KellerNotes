package com.justdoit.keller.common.proxy;
import com.justdoit.keller.common.response.Response;
import com.justdoit.keller.common.util.RequestUtil;
import com.justdoit.keller.common.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yangkaile
 * @date 2019-08-28 09:05:54
 * 接口转发
 */
@RestController
@RequestMapping("/form")
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class FormController {

    @Resource
    HttpServletRequest request;

    @Autowired
    RestTemplate restTemplate;

    // TODO 转发前校验JWT和用户权限

    /**
     * 表单形式的GET请求
     * @return
     */
    @GetMapping
    public ResponseEntity get(){
        Map<String,String> map = RequestUtil.getParam(request);
        ResponseEntity responseEntity ;
        try {
            String url = RequestUtil.getUrl(map,request);
            if(map == null){
                responseEntity = restTemplate.getForEntity(url, String.class);
            }else{
                responseEntity = restTemplate.getForEntity(url, String.class,map);
            }
            RequestUtil.successLog(request,responseEntity);
        }catch (Exception e){
            responseEntity = ResponseUtils.getResponseFromException(e);
            RequestUtil.errorLog(request,responseEntity);
        }
        return responseEntity;
    }


    /**
     * 表单形式的 POST 请求
     * @return
     */
    @PostMapping
    public ResponseEntity post(){
        Map<String,String> map = RequestUtil.getParam(request);
        ResponseEntity responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(RequestUtil.getUrl(map,request),null,String.class,map);
            RequestUtil.successLog(request,responseEntity);
        }catch (Exception e){
            responseEntity = ResponseUtils.getResponseFromException(e);
            RequestUtil.errorLog(request,responseEntity);
        }
        return responseEntity;
    }


    /**
     * 表单形式的 PUT  请求
     * @return
     */
    @PutMapping
    public ResponseEntity put(){
        Map<String,String> map = RequestUtil.getParam(request);
        ResponseEntity responseEntity = Response.ok();
        try {
            restTemplate.put(RequestUtil.getUrl(map,request),null,map);
            RequestUtil.successLog(request,responseEntity);
        }catch (Exception e){
            responseEntity = ResponseUtils.getResponseFromException(e);
            RequestUtil.errorLog(request,responseEntity);
        }
        return responseEntity;
    }

    /**
     * 表单形式的 DELETE 请求
     * restTemplate.delete()方法没有返回值，因此在delete操作时无法知道执行状态
     * @return
     */
    @DeleteMapping
    public ResponseEntity delete(){
        Map<String,String> map = RequestUtil.getParam(request);
        ResponseEntity responseEntity = Response.ok();
        try {
            restTemplate.delete(RequestUtil.getUrl(map,request),map);
            RequestUtil.successLog(request,responseEntity);
        }catch (Exception e){
            responseEntity = ResponseUtils.getResponseFromException(e);
            RequestUtil.errorLog(request,responseEntity);
        }
        return responseEntity;

    }

}
