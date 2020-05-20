package com.keller.common.util;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 多媒体文件元数据工具类，用来解析图片和视频的元数据
 * @author yangkaile
 * @date 2019-05-30 15:40:15
 */
public class MetadataUtils {
    /**
     * 图片高度
     */
    public static final String IMAGE_HEIGHT = "Exif Image Height";
    /**
     * 图片宽度
     */
    public static final String IMAGE_WIDTH = "Exif Image Width";
    /**
     * 拍摄时间
     */
    public static final String CREATE_TIME = "Date/Time Original";

    /**
     * 纬度
     */
    public static final String LATITUDE = "GPS Latitude";

    /**
     * 经度
     */
    public static final String LONGITUDE = "GPS Longitude";

    /**
     * 拍摄设备
     */
    public static final String MAKE = "Make";

    /**
     * 设备型号
     */
    public static final String MODEL = "Model";



    /**
     * 相机拍摄：
     * 拍摄时间  [Exif SubIFD] Date/Time Original
     * 图片宽度  [Exif SubIFD] Exif Image Width
     * 图片高度  [Exif SubIFD] Exif Image Height
     *
     * 纬度 [GPS] GPS Latitude - 40° 5' 5.49"
     * 经度 [GPS] GPS Longitude - 116° 21' 53.5"
     */
    public static Map<String,String> readJpeg(File file) {
        Metadata metadata;
        Map<String,String> result = new HashMap<>(16);
        try {
            metadata = JpegMetadataReader.readMetadata(file);
            Iterator<Directory> it = metadata.getDirectories().iterator();
            while (it.hasNext()) {
                Directory exif = it.next();
                Iterator<Tag> tags = exif.getTags().iterator();
                //遍历图片Exif信息的,取出需要的信息放入Map中
                while (tags.hasNext()) {
                    Tag tag = tags.next();
                    if(IMAGE_HEIGHT.equals(tag.getTagName())){
                        result.put(IMAGE_HEIGHT,tag.getDescription().substring(0,tag.getDescription().indexOf(" ")));
                    }
                    if(IMAGE_WIDTH.equals(tag.getTagName())){
                        result.put(IMAGE_WIDTH,tag.getDescription().substring(0,tag.getDescription().indexOf(" ")));
                    }
                    if(CREATE_TIME.equals(tag.getTagName())){
                        result.put(CREATE_TIME,tag.getDescription());
                    }
                    if(LATITUDE.equals(tag.getTagName())){
                        result.put(LATITUDE,tag.getDescription());
                    }
                    if(LONGITUDE.equals(tag.getTagName())){
                        result.put(LONGITUDE,tag.getDescription());
                    }
                    if(MAKE.equals(tag.getTagName())){
                        result.put(MAKE,tag.getDescription());
                    }
                    if(MODEL.equals(tag.getTagName())){
                        result.put(MODEL,tag.getDescription());
                    }
                    System.out.println(tag);
                }
            }
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static Map<String,String> readMp4(File file) {
        Metadata metadata;
        Map<String,String> result = new HashMap<>(16);
        try {
            metadata = Mp4MetadataReader.readMetadata(file);
            Iterator<Directory> it = metadata.getDirectories().iterator();
            while (it.hasNext()) {
                Directory exif = it.next();
                Iterator<Tag> tags = exif.getTags().iterator();
                //遍历图片Exif信息的,取出需要的信息放入Map中
                while (tags.hasNext()) {
                    Tag tag = tags.next();

                    System.out.println(tag);
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
