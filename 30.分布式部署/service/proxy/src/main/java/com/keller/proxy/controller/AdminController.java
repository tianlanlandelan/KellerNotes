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
 * @author yangkaile
 * @date 2020-05-19 20:04:56
 * 管理员功能入口
 */
@RestController
@RequestMapping(Constants.adminMapping)
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class AdminController {
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
        return HttpUtils.sendHttp(loadBalancer,rabbitTemplate,request,Constants.adminMapping);
    }

    /**
     * JSON  形式的 POST 请求
     * @return
     */
    @PostMapping
    public ResponseEntity post(){
        return HttpUtils.sendHttp(loadBalancer,rabbitTemplate,request,Constants.adminMapping);
    }

}
