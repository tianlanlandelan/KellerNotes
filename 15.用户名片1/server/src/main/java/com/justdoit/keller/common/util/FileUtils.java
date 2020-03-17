package com.justdoit.keller.common.util;

import com.justdoit.keller.common.config.PublicConstant;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author yangkaile
 * @date 2019-05-30 09:10:12
 */
public class FileUtils {
    /**
     * 文件后缀名分隔符
     */
    public static final String SUFFIX_SPLIT = ".";

    /**
     * 获取文件名的后缀，如 jpg/txt等
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(SUFFIX_SPLIT) + 1);
    }
    /**
     * 获取文件名的后缀和分隔符，如 .jpg/.txt等
     * @param fileName
     * @return
     */
    public static String getSuffixWithSpilt(String fileName){
        return fileName.substring(fileName.lastIndexOf(SUFFIX_SPLIT));
    }



    /**
     * 获取原图保存路径
     * @param fileName 文件名
     * @return 完整的保存路径
     */
    public static String getImgPath(String fileName){
        return PublicConstant.nginxPath + PublicConstant.imgPath + fileName;
    }

    /**
     * 获取原图访问地址
     * @param fileName
     * @return
     */
    public static String getImgUrl(String fileName){
        return PublicConstant.nginxUrl + PublicConstant.imgPath + fileName;
    }

    /**
     * 获取缩略图保存路径
     * @param fileName 文件名
     * @return 完整的保存路径
     */
    public static String getThumPath(String fileName){
        return PublicConstant.nginxPath + PublicConstant.thumPath + PublicConstant.THUM_PREFIX + fileName;
    }

    /**
     * 获取缩略图访问地址
     * @param fileName
     * @return
     */
    public static String getThumUrl(String fileName){
        return PublicConstant.nginxUrl + PublicConstant.thumPath + PublicConstant.THUM_PREFIX + fileName;
    }

    /**
     * 保存图片
     * @param file
     * @param name
     * @return
     * @throws IOException
     */
    public static String saveImg (MultipartFile file, String name) throws IOException {
        //解析文件后缀名
        String suffix = FileUtils.getSuffixWithSpilt(file.getOriginalFilename());
        //构建原图保存路径
        String fileName = name + suffix;
        //保存原图
        File img = new File(FileUtils.getImgPath(fileName));
        file.transferTo(img);
        return fileName;
    }

    /**
     * 保存图片和缩略图
     * @param file
     * @param name
     * @return
     * @throws IOException
     */
    public static String saveImgAndThum (MultipartFile file,String name) throws IOException{
        //解析文件后缀名
        String suffix = FileUtils.getSuffixWithSpilt(file.getOriginalFilename());
        //构建原图保存路径
        String fileName = name + suffix;
        //保存原图
        File img = new File(FileUtils.getImgPath(fileName));
        file.transferTo(img);

        //保存缩略图
        File thum = new File(FileUtils.getThumPath(fileName));
        Thumbnails.of(img).size(PublicConstant.THUM_MAX_WIDTH,PublicConstant.THUM_MAX_HEIGHT).toFile(thum);

        return fileName;
    }

    public static void main(String[] args){
        Console.print("getSuffix",getSuffix("abc.jpg"));
    }
}
