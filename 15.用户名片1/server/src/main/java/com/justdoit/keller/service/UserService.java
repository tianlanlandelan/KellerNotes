package com.justdoit.keller.service;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.util.JwtUtils;
import com.justdoit.keller.entity.EmailLog;
import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.mapper.UserMapper;
import com.justdoit.keller.common.response.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private EmailService emailService;

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
        if(sendType == PublicConstant.REGISTER_TYPE){
            if(userInfo != null){
                return ResultData.error("该邮箱已被注册");
            }

            //发送登录验证码或重置密码验证码时，要求邮箱已经注册
        }else {
            if(userInfo == null){
                return ResultData.error("账号不存在");
            }
        }
        return emailService.sendCode(sendType,email);
    }

    /**
     * 注册
     * @param userInfo
     * @param code
     * @return
     */
    public ResultData register(UserInfo userInfo,String code){

        EmailLog emailLog = new EmailLog();
        emailLog.setEmail(userInfo.getEmail());
        emailLog.setType(PublicConstant.REGISTER_TYPE);
        emailLog.setCode(code);
        if(emailService.checkCode(emailLog)){
            userInfo = insert(userInfo);
            if(userInfo == null){
                return ResultData.error("注册失败");
            }
            return ResultData.success(userInfo.getId());
        }
        return ResultData.error("验证码错误或已过期,请重新获取");
    }

    /**
     * 账号密码登录，登录成功后返回 JWT
     * @param userInfo
     * @return
     */
    public ResultData login(UserInfo userInfo){
        //指定查询条件
        UserInfo user = new UserInfo();
        user.setEmail(userInfo.getEmail());
        user.setType(userInfo.getType());
        user.setBaseKyleUseAnd(true);

        //查询用户
        List<UserInfo> list = userMapper.baseSelectByCondition(user);
        if(list == null || list.size() < 1){
            return ResultData.error("该用户尚未注册");
        }
        user = list.get(0);

        //校验密码
        if(user.getPassword().equals(userInfo.getPassword())){
            //返回 JWT
            return ResultData.success(JwtUtils.getJwtString(user));
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
        EmailLog emailLog = new EmailLog();
        emailLog.setEmail(email);
        emailLog.setType(PublicConstant.LOGIN_TYPE);
        emailLog.setCode(code);
        if(emailService.checkCode(emailLog)){
            UserInfo userInfo = getByEmailAndType(type,email);
            if(userInfo == null){
                return ResultData.error("用户不存在");
            }
            return ResultData.success(JwtUtils.getJwtString(userInfo));
        }
        return ResultData.error("验证码错误或已过期,请重新获取");
    }

    public ResultData getAll(){
        UserInfo userInfo = new UserInfo();
        List<UserInfo> list = userMapper.baseSelectAll(userInfo);
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
        List<UserInfo> list = userMapper.baseSelectByCondition(userInfo);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }


    private UserInfo insert(UserInfo userInfo){
        UserInfo user = getByEmailAndType(userInfo.getType(),userInfo.getEmail());
        if(user != null){
            return null;
        }
        userMapper.baseInsertAndReturnKey(userInfo);
        return userInfo;
    }
}
