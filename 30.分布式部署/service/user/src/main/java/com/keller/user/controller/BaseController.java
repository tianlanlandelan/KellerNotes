package com.keller.user.controller;

import com.keller.common.util.ObjectUtils;
import com.keller.user.entity.UserInfo;
import com.keller.user.service.UserService;
import com.keller.common.config.Constants;
import com.keller.common.http.Response;
import com.keller.common.http.RequestUtil;
import com.keller.common.util.StringUtils;
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
@RequestMapping("/base")
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class BaseController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private UserService userService;

    /**
     * 获取注册验证码
     * @param email
     * @return
     */
    @GetMapping("/getCodeForRegister")
    public ResponseEntity getCodeForRegister(String email){
        if(StringUtils.notEmail(email)){
            return Response.badRequest();
        }
        return Response.ok(userService.sendEmailCode(
                Constants.DEFAULT_USER_TYPE,Constants.REGISTER_TYPE,email));
    }

    /**
     * 发送登录验证码
     * @param type
     * @param email
     * @return
     */
    @GetMapping("/getCodeForLogin")
    public ResponseEntity getCodeForLogin(Integer type,String email){
        //验证邮箱和用户类型的合法性
        if(StringUtils.notEmail(email) || Constants.notUserType(type)){
            return Response.badRequest();
        }
        return Response.ok(userService.sendEmailCode(
                type,Constants.LOGIN_TYPE,email));
    }

    /**
     * 注册功能
     */
    @PostMapping("/register")
    public ResponseEntity register(String email,String password,String code){
        if(ObjectUtils.isEmpty(password,code) || StringUtils.notEmail(email)){
            return Response.badRequest();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        userInfo.setType(Constants.DEFAULT_USER_TYPE);

        return Response.ok(userService.register(userInfo,code,RequestUtil.getRealIp(request)));
    }

    /**
     * 账号密码登录
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity login(String email,String password,Integer type){
        if(ObjectUtils.isEmpty(password) || StringUtils.notEmail(email)){
            return Response.badRequest();
        }
        if(type == null){
            type = Constants.DEFAULT_USER_TYPE;
        }
        return Response.ok(userService.login(email,password,type,RequestUtil.getRealIp(request)));
    }

    /**
     * 验证码登录
     * @return
     */
    @PostMapping("/loginWithCode")
    public ResponseEntity loginWithCode(String email,String code,Integer type){
        //校验参数
        if(ObjectUtils.isEmpty(code) || StringUtils.notEmail(email) || Constants.notUserType(type)){
            return Response.badRequest();
        }
        return Response.ok(userService.loginWithCode(email,type,code));
    }

    /**
     * 通过邮件重置密码
     * @return
     */
    @PostMapping("/sendResetPasswordEmail")
    public ResponseEntity sendResetPasswordEmail(String email,Integer type){
        if(type == null){
            type = Constants.DEFAULT_USER_TYPE;
        }
        if(StringUtils.isEmail(email)){
            return Response.ok(userService.sendResetPasswordEmail(email,type));
        }
        return Response.badRequest();
    }

    /**
     * 通过邮件重置密码，上送参数中是密码
     * 从请求中解析 JWT
     * @return
     */
    @PostMapping("/resetPasswordByEmail")
    public ResponseEntity resetPasswordByEmail(String password,String token){
        if(token == null){
            return Response.badRequest();
        }
        return Response.ok(userService.resetPasswordByEmail(token,password));

    }
}
