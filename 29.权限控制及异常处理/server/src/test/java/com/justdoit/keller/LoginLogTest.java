package com.justdoit.keller;

import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.LoginLog;
import com.justdoit.keller.mapper.LoginLogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@SpringBootTest
public class LoginLogTest {


    @Resource
    private LoginLogMapper mapper;

    /**
     * 在login_log 表中添加测试数据
     */
    @Test
    public void initData(){
        Random random = new Random();
        for(int i = 0 ; i < 1000 ; i ++){
            LoginLog log = new LoginLog(random.nextInt(1000));
            log.setIp(random.nextInt(200) + 1 + "."
                    + random.nextInt(255) + "."
                    + random.nextInt(255) + "."
                    + random.nextInt(255));
            // 如果用 int 会造成计算出的毫秒值超过 Integer 的最大值
            long day = (long)random.nextInt(100);
            Console.println("day",day);

            log.setCreateTime(new Date(
                        System.currentTimeMillis() - (day * 24 * 60 * 60 * 1000)
                    )
            );
            mapper.baseInsertAndReturnKey(log);
        }
    }

}
