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
        Console.info("getJwt",JwtUtils.getJwtForLogin(userInfo));
    }

}
