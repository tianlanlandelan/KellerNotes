package com.justdoit.keller;

import com.justdoit.keller.entity.UserCard;
import com.justdoit.keller.mapper.UserCardMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserCardMapperTest {
    @Resource
    private UserCardMapper mapper;

    @Test
    public void createTable(){
        mapper.baseCreate(new UserCard());
    }
}
