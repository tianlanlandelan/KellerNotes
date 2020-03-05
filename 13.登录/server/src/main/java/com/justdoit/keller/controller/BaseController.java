package com.justdoit.keller.controller;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.response.Response;
import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 不需要登录就能调用的接口
 * @author yangkaile
 * @date 2019-10-09 09:24:19
 */
@RestController
@RequestMapping("/base")
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class BaseController {
    @Resource
    private UserService userService;

    /**
     * 注册功能
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Map<String,String> params){
        Console.info("register",params);
        String email = params.get("email");
        String password = params.get("password");
        String code = params.get("code");
        if(StringUtils.isEmpty(password,code) || StringUtils.notEmail(email)){
            return Response.badRequest();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        userInfo.setType(PublicConstant.DEFAULT_USER_TYPE);

        return Response.ok(userService.register(userInfo,code));
    }

    /**
     * 获取验证码
     * @param email
     * @return
     */
    @GetMapping("/getCodeForRegister")
    public ResponseEntity getCode(String email){
        Console.info("getCodeForRegister",email);
        if(StringUtils.notEmail(email)){
            return Response.badRequest();
        }
        return Response.ok(userService.sendRegisterCode(PublicConstant.DEFAULT_USER_TYPE,email));
    }
}
