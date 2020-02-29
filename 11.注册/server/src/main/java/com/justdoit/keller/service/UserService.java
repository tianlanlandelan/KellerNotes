package com.justdoit.keller.service;

import com.justdoit.keller.common.config.PublicConstant;
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
     * 发送注册验证码
     * 根据用户类型和邮箱判断用户是否已经注册，如果已注册，返回：该邮箱已注册
     *  否则，发送邮件验证码
     * @param type 用户类型
     * @param email 邮箱
     * @return
     */
    public ResultData sendRegisterCode(int type,String email){
        UserInfo userInfo = getByEmailAndType(type,email);
        if(userInfo != null){
            return ResultData.error("该邮箱已被注册");
        }
        return emailService.sendCode(PublicConstant.REGISTER_TYPE,email);
    }


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


    public ResultData getAll(){
        UserInfo userInfo = new UserInfo();
        List<UserInfo> list = userMapper.baseSelectAll(userInfo);
        if(list == null || list.size() < 1){
            return ResultData.error("没有数据");
        }
        return ResultData.success(list);
    }

    public ResultData checkRegister(int type,String email){
        UserInfo userInfo = getByEmailAndType(type,email);
        if(userInfo == null){
            return ResultData.success();
        }
        return ResultData.error("用户已存在");
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
