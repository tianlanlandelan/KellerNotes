package com.justdoit.keller.common.proxy;
import com.justdoit.keller.common.util.RequestUtil;
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

    /**
     * 表单形式的GET请求
     * @return
     */
    @GetMapping
    public ResponseEntity get(){
        Map<String,Object> params = RequestUtil.getParam(request);
        return RequestUtil.doGet(request,params,restTemplate);
    }


    /**
     * 表单形式的 POST 请求
     * @return
     */
    @PostMapping
    public ResponseEntity post(){
        Map<String,Object> params = RequestUtil.getParam(request);
        return RequestUtil.doPost(request,params,restTemplate);
    }
}
