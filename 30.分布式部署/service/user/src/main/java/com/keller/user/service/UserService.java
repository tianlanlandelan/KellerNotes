package com.keller.user.service;

import com.alibaba.fastjson.JSON;
import com.keller.common.entity.LoginLog;
import com.keller.common.http.*;
import com.keller.common.util.ErrorLogUtils;
import com.keller.common.util.ObjectUtils;
import com.keller.user.EmailSender;
import com.keller.user.entity.UserCard;
import com.keller.user.entity.UserInfo;
import com.keller.user.mapper.UserCardMapper;
import com.keller.user.mapper.UserMapper;
import com.keller.common.config.Constants;
import com.keller.common.config.MqConstants;
import com.keller.common.util.JwtUtils;
import com.keller.common.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author yangkaile
 * @date 2020-04-29 08:35:01
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCardMapper userCardMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private LoadBalancerClient loadBalancerClient;


    /**
     * 发送邮件验证码
      * @param userType 用户类型
     * @param sendType  验证码类型
     * @param email 邮箱地址
     * @return
     */
    public ResultData sendEmailCode(int userType,int sendType,String email){
        UserInfo userInfo = getByEmailAndType(userType,email);

        //发送注册验证码时，要求邮箱尚未注册
        if(sendType == Constants.REGISTER_TYPE){
            if(userInfo != null){
                return ResultData.error("该邮箱已被注册");
            }

            //发送登录验证码或重置密码验证码时，要求邮箱已经注册
        }else {
            if(userInfo == null){
                return ResultData.error("账号不存在");
            }
        }
        if(new EmailSender().sendCode(sendType,email)){
            return ResultData.error("邮件发送失败");
        }
        return ResultData.success("邮件已发送");
    }

    /**
     * 注册，注册成功后返回 JWT
     * @param userInfo
     * @param code
     * @return
     */
    public ResultData register(UserInfo userInfo,String code,String ip){
        if(checkCode(userInfo.getEmail(),code,Constants.REGISTER_TYPE)){
            userInfo = insert(userInfo);
            if(userInfo == null){
                return ResultData.error("注册失败");
            }
            LoginLog loginLog = new LoginLog(userInfo.getId());
            loginLog.setIp(ip);
            // 注册成功后记录注册日志
            rabbitTemplate.convertAndSend(MqConstants.REGISTER_LOG, JSON.toJSONString(loginLog));
            //创建用户名片
            userCardMapper.baseInsert(new UserCard(userInfo.getId()));
            // 返回 JWT ，注册成功时返回 JWT,用户可以不用再做一遍登录操作
            return ResultData.success(JwtUtils.getJwtString(
                    userInfo.getId(),userInfo.getEmail(),userInfo.getType(),
                    Constants.LOGIN_TYPE,null));
        }
        return ResultData.error("验证码错误或已过期,请重新获取");
    }

    private boolean checkCode(String email,String code,int type){
        // TODO 从 log 中读取邮件发送记录

        String url = ServerRouter.getServeUrl(loadBalancerClient,ServerRouter.LOG);
        StringBuilder builder = new StringBuilder();
        builder.append(ServerRouter.getServeUrl(loadBalancerClient,ServerRouter.LOG))
                .append("/emailLog?email=")
                .append(email)
                .append("&code=")
                .append(code)
                .append("&type=")
                .append(type);
        ResponseEntity<String> responseEntity;
        try{
            responseEntity = RestTemplateUtils.getInstance().getForEntity(builder.toString(),String.class);
        }catch (Exception e){
            responseEntity = ResponseUtils.getResponseFromException(e);
        }
        ErrorLogUtils.getInstance(rabbitTemplate)
                .requestLog(ServerRouter.USER, RequestUtil.GET,url,null,responseEntity);

        return true;
    }

    /**
     * 账号密码登录，登录成功后返回 JWT
     * @param email
     * @param password
     * @param type
     * @return
     */
    public ResultData login(String email,String password,int type,String ip){
        //指定查询条件
        UserInfo user = new UserInfo();
        user.setEmail(email);
        user.setType(type);
        user.setBaseKyleUseAnd(true);
        //查询用户
        List<UserInfo> list = userMapper.baseSelectList(user);
        if(list == null || list.size() < 1){
            return ResultData.error("该用户尚未注册");
        }
        user = list.get(0);

        //校验密码
        if(user.getPassword().equals(password)){
            LoginLog loginLog = new LoginLog(user.getId());
            loginLog.setIp(ip);
            String message = JSON.toJSONString(loginLog);
            rabbitTemplate.convertAndSend(MqConstants.LOGON_LOG,message);
            //返回 JWT
            return ResultData.success(
                    JwtUtils.getJwtString(
                    user.getId(),user.getEmail(),user.getType(),
                    Constants.LOGIN_TYPE,null));
        }
        return ResultData.error("账号密码错误");
    }

    /**
     * 验证码登录
     * @param email
     * @param type
     * @param code
     * @return
     */
    public ResultData loginWithCode(String email,int type,String code){
        if(checkCode(email,code,Constants.LOGIN_TYPE)){
            UserInfo userInfo = getByEmailAndType(type,email);
            if(userInfo == null){
                return ResultData.error("用户不存在");
            }
            return ResultData.success(JwtUtils.getJwtString(
                    userInfo.getId(),userInfo.getEmail(),userInfo.getType(),
                    Constants.LOGIN_TYPE,null));
        }
        return ResultData.error("验证码错误或已过期,请重新获取");
    }

    /**
     * 发送重置密码邮件
     * @param email
     * @return
     */
    public ResultData sendResetPasswordEmail(String email,int type){
        UserInfo userInfo = getByEmailAndType(type,email);
        if(userInfo == null){
            return ResultData.error("该邮箱尚未注册");
        }
        String code = StringUtils.getAllCharString(Constants.EMAIL_CODE_LENGTH);
        String token = JwtUtils.getJwtString(
                userInfo.getId(),userInfo.getEmail(),
                userInfo.getType(),Constants.RESET_PASSWORD_TYPE,code);
        if(new EmailSender().sendResetPasswordEmail(email,code,token)){
            return ResultData.error("邮件发送失败");
        }
        return ResultData.error("邮件发送成功");
    }

    /**
     * 通过邮件重置密码
     * 校验 JWT，从中解析出 邮箱、用户类型、验证码
     * 然后去 邮件发送记录中校验验证码
     * 校验成功后重置密码
     * @param token
     * @param password
     * @return
     */
    public ResultData resetPasswordByEmail(String token,String password){
        HashMap<String,String> hashMap = JwtUtils.readJwt(token);
        if(hashMap == null){
            return ResultData.error("参数错误");
        }
        String userEmail = hashMap.get(JwtUtils.EMAIL);
        String userCode = hashMap.get(JwtUtils.CODE);
        Integer userType = Integer.parseInt(hashMap.get(JwtUtils.USER_TYPE));

        UserInfo result = getByEmailAndType(userType,userEmail);
        if(result == null){
            return ResultData.error("该邮箱尚未注册");
        }
        if(ObjectUtils.isEmpty(userCode,userEmail)){
            return ResultData.error("身份验证失败");
        }
        if(checkCode(userEmail,userCode,Constants.RESET_PASSWORD_TYPE)){
            result.setPassword(password);
            result.setUpdateTime(new Date());
            result.setUpdateUserId(result.getId());

            userMapper.baseUpdateById(result);

            return ResultData.success();
        }
        return ResultData.error("密码重置失败，请重试");
    }

    public ResultData getAll(){
        UserInfo userInfo = new UserInfo();
        List<UserInfo> list = userMapper.baseSelectList(userInfo);
        if(list == null || list.size() < 1){
            return ResultData.error("没有数据");
        }
        return ResultData.success(list);
    }

    /**
     * 根据类型和邮箱查询用户
     * @param type
     * @param email
     * @return
     */
    private UserInfo getByEmailAndType(int type,String email){
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setType(type);
        userInfo.setBaseKyleUseAnd(true);
        List<UserInfo> list = userMapper.baseSelectList(userInfo);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }


    /**
     * 添加用户
     * @param userInfo
     * @return
     */
    private UserInfo insert(UserInfo userInfo){
        UserInfo user = getByEmailAndType(userInfo.getType(),userInfo.getEmail());
        if(user != null){
            return null;
        }
        userMapper.baseInsertAndReturnKey(userInfo);
        return userInfo;
    }

    public ResultData getUserList(int page,int pageSize,String email){
        UserInfo userInfo = new UserInfo();
        userInfo.setBaseKyleCurrentPage(page);
        userInfo.setBaseKylePageSize(pageSize);
        userInfo.setBaseKyleDetailed(false);
        //如果传入了email参数，则执行模糊查询
        if(ObjectUtils.noEmpty(email)){
            userInfo.setEmail(email);
            userInfo.setBaseKyleCustomCondition(UserMapper.whereEmailLike);
        }
        List<UserInfo> list = userMapper.baseSelectPageList(userInfo);
        return ResultData.success(list);
    }

    public ResultData getUserCounter(String email){
        UserInfo userInfo = new UserInfo();
        //如果传入了email参数，则执行模糊查询
        if(ObjectUtils.noEmpty(email)){
            userInfo.setEmail(email);
            userInfo.setBaseKyleCustomCondition(UserMapper.whereEmailLike);
        }
        Integer counter  = userMapper.baseSelectCount(userInfo);
        return ResultData.success(counter);
    }
}
