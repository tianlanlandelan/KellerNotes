package com.keller.common.util;

import com.keller.common.config.Constants;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class ImageUtils {


    /**
     * 保存图片
     * @param file  文件
     * @param noteId    笔记ID
     * @param userId    用户ID
     * @return
     * @throws IOException
     */
    public static String saveImg (MultipartFile file,int noteId,int userId) throws IOException {
        //解析文件后缀名
        String suffix = FileUtils.getSuffixWithSpilt(file.getOriginalFilename());

        String timeMask = DateUtils.getTimeMask();
        //构建图片名称
        String fileName = noteId +  "-" + timeMask + suffix;
        //保存图片
        File img = new File(FileUtils.getImgPath(fileName,userId));
        file.transferTo(img);
        return fileName;
    }



    /**
     * 保存用户头像
     * 用户头像保存为固定的文件名，设置新的头像时，会覆盖掉原来的头像
     * @param file
     * @param userId
     * @return
     * @throws IOException
     */
    public static String savePortrait(MultipartFile file, int userId) throws IOException{
        //解析文件后缀名
        String suffix = FileUtils.getSuffix(file.getOriginalFilename());
        String fileName = FileUtils.getPortraitName(userId);

        //保存原图
        File img = new File(FileUtils.getPortraitPath(fileName));
        if(img.exists()){
            img.delete();
        }
        //保存缩略图
        File thum = new File(FileUtils.getPortraitThumPath(fileName));
        if(thum.exists()){
            thum.delete();
        }
        //如果图片是 PNG 格式的，直接存储
        if(Constants.PORTRAIT_TYPE.equals(suffix)){
            file.transferTo(img);
        }else{
            /*
             将其他格式的图片转换成 PNG 格式
              */
            Thumbnails.of(file.getInputStream()).scale(1).outputFormat(Constants.PORTRAIT_TYPE).toFile(img);
        }
        //生成缩略图
        Thumbnails.of(img).size(Constants.THUM_MAX_WIDTH,Constants.THUM_MAX_HEIGHT).toFile(thum);
        return fileName;
    }



    public static void main(String[] args) throws Exception {
        String strUrl = "F:\\测试图片.jpg";
        File file = new File(strUrl);
        if(file.exists()){
            file.delete();
        }
    }


}
