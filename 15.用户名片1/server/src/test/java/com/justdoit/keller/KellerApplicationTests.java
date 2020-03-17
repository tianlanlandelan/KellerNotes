package com.justdoit.keller;

import com.justdoit.keller.entity.UserCard;
import com.justdoit.keller.mapper.UserCardMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class KellerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private UserCardMapper userCardMapper;

    @Test
    public void createUserCardTable(){
        userCardMapper.baseCreate(new UserCard());
    }

}
