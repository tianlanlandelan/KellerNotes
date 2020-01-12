package com.justdoit.keller.controller;

import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping
    public ResponseEntity getAll(){
        List<UserInfo> list = userService.getAll();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }
}
