package com.keller.proxy.controller;

import com.keller.common.config.Constants;
import com.keller.proxy.util.HttpUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 不需要登录就能调用的接口
 * @author yangkaile
 * @date 2019-10-09 09:24:19
 */
@RestController
@RequestMapping(Constants.baseMapping)
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class BaseController {
    @Resource
    private LoadBalancerClient loadBalancer;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    HttpServletRequest request;


    /**
     * json 格式的GET请求
     * @return
     */
    @GetMapping
    public ResponseEntity get(){
        return HttpUtils.sendHttp(loadBalancer,rabbitTemplate,request,Constants.baseMapping);
    }

    /**
     * JSON  形式的 POST 请求
     * @return
     */
    @PostMapping
    public ResponseEntity post(){
        return HttpUtils.sendHttp(loadBalancer,rabbitTemplate,request,Constants.baseMapping);
    }

}
