package com.keller.log.controller;

import com.keller.common.http.Response;
import com.keller.common.util.ObjectUtils;
import com.keller.log.service.EmailLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yangkaile
 * @date 2020-05-20 09:29:55
 *
 */
@RestController
@RequestMapping("/emailLog")
public class EmailLogController {

    @Resource
    private EmailLogService service;

    @GetMapping
    public ResponseEntity checkCode(String email,String code,Integer type){
        if(ObjectUtils.isEmpty(email,code,type)){
            return Response.badRequest();
        }
        return Response.ok(service.checkCode(email, code, type));
    }

}
