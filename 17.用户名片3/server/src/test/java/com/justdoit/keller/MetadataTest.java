package com.justdoit.keller;

import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.MetadataUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

public class MetadataTest {
    @Test
    public void readJpeg(){
        File jpegFile = new File("/Users/yangkaile/Pictures/IMG_20190127_163451.JPG");
        Map<String,String> map = MetadataUtils.readJpeg(jpegFile);

        Console.println("readJpeg");
        for(String key:map.keySet()){
            System.out.println(key+ ":" + map.get(key));
        }

    }
}
