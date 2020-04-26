package com.justdoit.keller;

import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileUtilsTest {

    @Test
    public void getPortraitPath(){
        int userId = 10;
        String fileName = FileUtils.getPortraitName(userId);
        String portraitPath = FileUtils.getPortraitPath(fileName);
        String portraitUrl = FileUtils.getPortraitUrl(fileName);
        String portraitThumPath = FileUtils.getPortraitThumPath(fileName);
        String portraitThumUrl = FileUtils.getPortraitThumUrl(fileName);

        Console.println("getPortraitPath",fileName,portraitPath,portraitUrl,portraitThumPath,portraitThumUrl);
    }

    @Test
    public void getImgPath(){
        int userId = 10;
        String fileName = "a.jpg";
        String filePath = FileUtils.getImgPath(fileName,userId);

        Console.println("getImgPath",filePath);
    }



}
