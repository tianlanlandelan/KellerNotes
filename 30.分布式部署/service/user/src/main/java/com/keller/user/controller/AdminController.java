package com.keller.user.controller;

import com.keller.common.util.Console;
import com.keller.common.util.ObjectUtils;
import com.keller.user.service.UserService;
import com.keller.common.http.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private UserService userService;


    @GetMapping("/list")
    public ResponseEntity getUserList(Integer kellerAdminId, Integer page, Integer size, String email){
        Console.println("userList");
        if(ObjectUtils.isEmpty(kellerAdminId,page,size)){
            return Response.badRequest();
        }
        return Response.ok(userService.getUserList(page,size,email));
    }


    @GetMapping("/counter")
    public ResponseEntity getUserCounter(Integer kellerAdminId,String email){

        if(ObjectUtils.isEmpty(kellerAdminId)){
            return Response.badRequest();
        }
        return Response.ok(userService.getUserCounter(email));
    }

}
