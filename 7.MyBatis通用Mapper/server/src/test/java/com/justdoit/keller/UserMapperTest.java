package com.justdoit.keller;

import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.mapper.UserMapper;
import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void initTestData(){
        for(int i = 0 ; i < 10 ; i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setType(1);
            userInfo.setEmail(StringUtils.getAllCharString(10));
            userInfo.setPassword("123456");
            userMapper.baseInsertAndReturnKey(userInfo);
            Console.println(userInfo.getId() + "",userInfo);
        }
    }

    @Test
    public void getAll(){
        List<UserInfo> list = userMapper.baseSelectAll(new UserInfo());
        for(UserInfo userInfo : list){
            Console.println(userInfo.getId() + "",userInfo);
        }
    }
}
