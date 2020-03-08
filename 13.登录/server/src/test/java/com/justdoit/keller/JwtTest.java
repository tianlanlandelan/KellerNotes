package com.justdoit.keller;

import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.JwtUtils;
import com.justdoit.keller.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {
    @Test
    public void getJwt(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1001);
        userInfo.setEmail("abc@de.fg");
        userInfo.setType(1);
        Console.info("getJwt",JwtUtils.getJwtString(userInfo));
    }
    @Test
    public void getUser(){
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoiS2VsbGVyTm90ZXMiLCJpYXQiOjE1ODM0NjgxOTUsImV4cCI6MTU4MzQ2OTM5NSwidXNlck5hbWUiOiJhYmNAZGUuZmciLCJ1c2VyVHlwZSI6MX0.GUv05KjGRV8gRA0OMsmAIbwzYioHX90LJnWh6av1ao";
        Console.info("getUser",JwtUtils.getUser(jwt));
    }
}
