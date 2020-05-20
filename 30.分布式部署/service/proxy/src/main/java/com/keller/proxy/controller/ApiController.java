package com.keller.proxy.controller;
import com.keller.proxy.util.HttpUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yangkaile
 * @date 2019-08-28 09:05:54
 * 接口转发
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class ApiController {
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
        return HttpUtils.sendHttp(loadBalancer,rabbitTemplate,request,null);
    }


    /**
     * JSON  形式的 POST 请求
     * @return
     */
    @PostMapping
    public ResponseEntity post(){
        return HttpUtils.sendHttp(loadBalancer,rabbitTemplate,request,null);
    }
}
