package com.keller.common.util;

import com.keller.common.config.Constants;

import java.io.File;

/**
 * 文件处理工具类，获取文件名、后缀名、保存文件等
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
     * 获取头像文件名
     * @param userId
     * @return 文件名，不带后缀
     */
    public static String getPortraitName(int userId){
        return Md5Utils.getMd5String(Constants.PORTRAIT_PREFIX + userId) ;
    }

    /**
     * 获取头像保存路径
     * @param fileName 文件名，不带后缀
     * @return  完整的头像保存路径
     */
    public static String getPortraitPath(String fileName){
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.nginxPath)
                .append(Constants.portraitPath)
                .append(Constants.originImgPath);

        if(mkdirs(builder.toString())){
            builder.append(fileName)
                    .append(".")
                    .append(Constants.PORTRAIT_TYPE);
            return builder.toString();
        }else {
            return null;
        }
    }

    /**
     * 获取头像访问地址
     * @param fileName
     * @return
     */
    public static String getPortraitUrl(String fileName){
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.nginxUrl)
                .append(Constants.portraitPath)
                .append(Constants.originImgPath)
                .append(fileName)
                .append(".")
                .append(Constants.PORTRAIT_TYPE);
            return builder.toString();
    }

    /**
     * 获取头像缩略图保存路径
     * @param fileName
     * @return
     */
    public static String getPortraitThumPath(String fileName){

        StringBuilder builder = new StringBuilder();
        builder.append(Constants.nginxPath)
                .append(Constants.portraitPath)
                .append(Constants.thumbnailPath);

        if(mkdirs(builder.toString())){
            builder.append(fileName)
                    .append(".")
                    .append(Constants.THUM_PREFIX)
                    .append(".")
                    .append(Constants.PORTRAIT_TYPE);
            return builder.toString();
        }else {
            return null;
        }
    }

    /**
     * 获取头像缩略图访问地址
     * @param fileName
     * @return
     */
    public static String getPortraitThumUrl(String fileName){

        StringBuilder builder = new StringBuilder();
        builder.append(Constants.nginxUrl)
                .append(Constants.portraitPath)
                .append(Constants.thumbnailPath)
                .append(fileName)
                .append(".")
                .append(Constants.THUM_PREFIX)
                .append(".")
                .append(Constants.PORTRAIT_TYPE);
        return builder.toString();
    }

    /**
     * 获取图片保存路径
     * @param fileName 文件名
     * @return 完整的保存路径
     */
    public static String getImgPath(String fileName,int userId){
        String directory = getUserImgDirectory(userId);
        if(directory.isEmpty()){
            return null;
        }else {
            return directory + fileName;
        }
    }



    /**
     * 获取原图访问地址
     * @param fileName
     * @return
     */
    public static String getImgUrl(String fileName,int userId){
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.nginxUrl)
                .append(Constants.imgPath)
                .append(userId)
                .append("/")
                .append(fileName);

        return builder.toString();
    }


    /**
     * 删除用户的所有图片
     * @param fileName
     * @param userId
     */
    public static void deleteImg(String fileName,int userId){
        File file = new File(getUserImgDirectory(userId) + fileName);
        if(file.exists() && file.isFile()){
            file.delete();
        }else {
            return ;
        }
    }

    /**
     * 删除笔记中的图片
     * @param userId
     * @param noteId
     */
    public static void deleteImgByNoteId(int userId,int noteId){
        String noteIdStr = noteId + "";
        String[] imgs = getUserImgs(userId);
        if(imgs == null || imgs.length < 1){
            return;
        }
        for(String imgName :imgs){
            String[] name = imgName.split("-");
            if(name.length < 2){
                continue;
            }
            if(noteIdStr.equals(name[0])){
                deleteImg(imgName,userId);
            }
        }
    }

    /**
     * 获取用户存储的所有图片名称
     * @param userId
     * @return
     */
    private static String[] getUserImgs (int userId){
        File file = new File(getUserImgDirectory(userId));
        if(file.exists() && file.isDirectory()){
            return file.list();
        }else{
            return null;
        }
    }

    /**
     * 获取用户文件目录
     * @param userId
     * @return
     */
    private static String getUserImgDirectory(int userId){
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.nginxPath)
                .append(Constants.imgPath)
                .append(userId)
                .append("/");
        if(mkdirs(builder.toString())){
            return builder.toString();
        }else{
            return null;
        }
    }

    /**
     * 创建多级目录
     * @param path
     * @return
     */
    private static boolean mkdirs(String path){
        File directory = new File(path);
        if(directory.exists() && directory.isDirectory()){
            return true;
        }else {
            return directory.mkdirs();
        }
    }

}
