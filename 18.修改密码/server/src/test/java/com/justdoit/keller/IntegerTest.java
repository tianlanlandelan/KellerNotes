package com.justdoit.keller;


import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.concurrent.atomic.AtomicInteger;

public class IntegerTest {
    @BeforeTestMethod
    public void before(){
        AtomicInteger value = new AtomicInteger(1);

        Integer a = 1;
        a.intValue();
    }


    @Test
    public void a(){
        Integer a = 1;
        if(a == 1){

        }
    }


}
