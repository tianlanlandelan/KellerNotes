package com.justdoit.keller;

import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class WhereAttributeTest {
    @Resource
    private UserMapper userMapper;
    @Test
    public void test(){
        UserInfo entity = new UserInfo();
        entity.setBaseKyleCustomCondition(UserMapper.whereEmailLike);
        entity.setEmail("test");

        List<UserInfo> list = userMapper.baseSelectList(entity);

        for(UserInfo userInfo:list){
            Console.println(userInfo.getEmail(),userInfo);
        }
    }

}
