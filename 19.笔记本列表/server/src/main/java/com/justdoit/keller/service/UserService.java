package com.justdoit.keller.service;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.util.JwtUtils;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.UserCard;
import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.mapper.UserCardMapper;
import com.justdoit.keller.mapper.UserMapper;
import com.justdoit.keller.common.response.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private EmailService emailService;

    @Resource
    private UserCardMapper userCardMapper;

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
     * 注册，注册成功后返回 JWT
     * @param userInfo
     * @param code
     * @return
     */
    public ResultData register(UserInfo userInfo,String code){
        if(emailService.checkCode(userInfo.getEmail(),code,PublicConstant.REGISTER_TYPE)){
            userInfo = insert(userInfo);
            if(userInfo == null){
                return ResultData.error("注册失败");
            }
            //创建用户名片
            userCardMapper.baseInsert(new UserCard(userInfo.getId()));
            // 返回 JWT ，注册成功时返回 JWT,用户可以不用再做一遍登录操作
            return ResultData.success(JwtUtils.getJwtForLogin(userInfo));
        }
        return ResultData.error("验证码错误或已过期,请重新获取");
    }

    /**
     * 账号密码登录，登录成功后返回 JWT
     * @param email
     * @param password
     * @param type
     * @return
     */
    public ResultData login(String email,String password,int type){
        //指定查询条件
        UserInfo user = new UserInfo();
        user.setEmail(email);
        user.setType(type);
        user.setBaseKyleUseAnd(true);

        //查询用户
        List<UserInfo> list = userMapper.baseSelectByCondition(user);
        if(list == null || list.size() < 1){
            return ResultData.error("该用户尚未注册");
        }
        user = list.get(0);

        //校验密码
        if(user.getPassword().equals(password)){
            //返回 JWT
            return ResultData.success(JwtUtils.getJwtForLogin(user));
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
        if(emailService.checkCode(email,code,PublicConstant.LOGIN_TYPE)){
            UserInfo userInfo = getByEmailAndType(type,email);
            if(userInfo == null){
                return ResultData.error("用户不存在");
            }
            return ResultData.success(JwtUtils.getJwtForLogin(userInfo));
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
        String code = StringUtils.getAllCharString(PublicConstant.EMAIL_CODE_LENGTH);
        String token = JwtUtils.getJwtForResetPassword(userInfo,code);
        return emailService.sendResetPasswordEmail(email,code,token);
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
        String userEmail = JwtUtils.getEmailForResetPassword(token);
        String userCode = JwtUtils.getCodeForResetPassword(token);
        Integer userType = JwtUtils.getUserTypeForResetPassword(token);

        UserInfo result = getByEmailAndType(userType,userEmail);
        if(result == null){
            return ResultData.error("该邮箱尚未注册");
        }
        if(StringUtils.isEmpty(userCode,userEmail)){
            return ResultData.error("身份验证失败");
        }
        if(emailService.checkCode(userEmail,userCode,PublicConstant.RESET_PASSWORD_TYPE)){
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
