package com.justdoit.keller.controller;

import com.justdoit.keller.common.response.Response;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.UserCard;
import com.justdoit.keller.service.UserCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yangkaile
 * @date 2020-03-14 15:38:50
 * 用户名片接口
 */
@RestController
@RequestMapping("/userCard")
public class UserCardController {
    @Resource
    private UserCardService service;

    /**
     * 设置用户名片
     * @param kellerUserId 用户ID
     * @param nickName 用户昵称
     * @param email  要展示的邮件地址
     * @param label  心情
     * @return
     */
    @PostMapping
    public ResponseEntity setUserCard(Integer kellerUserId,String nickName,String email,String label){
        if(kellerUserId != null && StringUtils.hasValue(nickName,email,label)){
            return Response.ok(
                    service.setUserCard(kellerUserId,nickName,email,label));
        }
        return Response.badRequest();
    }

    @GetMapping
    public ResponseEntity getUserCard(Integer kellerUserId){
        if(kellerUserId != null){
            return Response.ok(service.getUserCard(kellerUserId));
        }
        return Response.badRequest();
    }
}
