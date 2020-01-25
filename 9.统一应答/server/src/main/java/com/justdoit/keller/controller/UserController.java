package com.justdoit.keller.controller;

import com.justdoit.keller.response.Response;
import com.justdoit.keller.response.ResultData;
import com.justdoit.keller.service.UserService;
import com.justdoit.keller.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping
    public ResponseEntity getAll(){
        ResultData resultData = userService.getAll();
        return Response.ok(resultData);
    }

    @GetMapping("/getByEmail")
    public ResponseEntity getByEmail(String email){
        if(StringUtils.isEmpty(email)){
            return Response.badRequest();
        }
        ResultData resultData = userService.getByEmail(email);
        return Response.ok(resultData);
    }
}
