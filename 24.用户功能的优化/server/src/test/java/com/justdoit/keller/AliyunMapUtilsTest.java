package com.justdoit.keller;

import com.justdoit.keller.common.util.AliyunMapUtils;
import com.justdoit.keller.common.util.Console;
import org.junit.jupiter.api.Test;

public class AliyunMapUtilsTest {

    @Test
    public  void getAddress(){
        String lat = "39° 59' 20.71\"";
        String log = "116° 16' 20.36\"";
        Console.println("getAddress", AliyunMapUtils.getAddress(log,lat));
    }
}
