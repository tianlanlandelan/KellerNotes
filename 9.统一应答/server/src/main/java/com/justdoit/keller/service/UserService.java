package com.justdoit.keller.service;

import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.mapper.UserMapper;
import com.justdoit.keller.response.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public ResultData getAll(){
        UserInfo userInfo = new UserInfo();
        List<UserInfo> list = userMapper.baseSelectAll(userInfo);
        if(list == null || list.size() < 1){
            return ResultData.error("没有数据");
        }
        return ResultData.success(list);
    }

    public ResultData getByEmail(String email){
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        List<UserInfo> list = userMapper.baseSelectByCondition(userInfo);
        if(list != null && list.size() > 0){
            return ResultData.success(list.get(0));
        }
        return ResultData.error("该邮箱未注册");
    }
}
