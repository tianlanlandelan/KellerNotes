package com.keller.common.util;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 规定统一的MD5加密方法
 * @author yangkaile
 * @date 2020-04-20 11:11:42
 *
 */
public class Md5Utils {

    /**
     * 加密字符串
     * @param str 原字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException,UnsupportedEncodingException 加密过程中抛的异常
     */
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException,UnsupportedEncodingException {
        return new BASE64Encoder().encode(
                MessageDigest.getInstance("MD5").digest(str.getBytes("utf-8"))
        );
    }

    /**
     * 16进制字符
     */
    static String[] chars = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    /**
     * 将普通字符串用md5加密，并转化为16进制字符串
     * @param str
     * @return
     */
    public static String getMd5String(String str) {

        try {
            // 参数代表的是算法名称
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] result = md5.digest(str.getBytes());

            StringBuilder sb = new StringBuilder(32);
            // 将结果转为16进制字符  0~9 A~F
            for (int i = 0; i < result.length; i++) {
                // 一个字节对应两个字符
                byte x = result[i];
                // 取得高位
                int h = 0x0f & (x >>> 4);
                // 取得低位
                int l = 0x0f & x;
                sb.append(chars[h]).append(chars[l]);
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查原字符串和加密后的字符串是否匹配
     * @param str   原字符串
     * @param encodeStr 加密后的字符
     * @return 匹配 true,不匹配 false
     * @throws NoSuchAlgorithmException,UnsupportedEncodingException 加密过程中可能抛异常
     */
    public static boolean checkQual(String str,String encodeStr){
        try {
            if(EncoderByMd5(str).equals(encodeStr)){
                return true;
            }
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return false;
    }

}
