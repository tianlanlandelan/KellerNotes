package com.justdoit.keller.service;

import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public List<UserInfo> getAll(){
        return userMapper.selectAll();
    }
}
