package com.justdoit.keller.common.util;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author yangkaile
 * @date 2020-03-05 17:05:11
 *
 * Jwt 工具类，用于生成及解析JWT
 */
public class JwtUtils {

    private static String userNameKey = "userName";

    private static String userTypeKey = "userType";

    /**
     * 生成 JWT
     * @return
     */
    public static String getJwtString(UserInfo userInfo){
        long now=System.currentTimeMillis();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(userInfo.getId() + "")
                //设置应用名
                .setSubject(PublicConstant.appName)
                //签发时间
                .setIssuedAt( new Date() )
                //过期时间
                .setExpiration( new Date( now + PublicConstant.JWT_EXP_TIME ) )
                //自定义内容
                .claim(userNameKey,userInfo.getEmail())
                .claim(userTypeKey,userInfo.getType())
                //签名 密钥
                .signWith( SignatureAlgorithm.HS256, PublicConstant.JWT_SIGN_KEY);
        return jwtBuilder.compact();
    }

    /**
     * 校验JWT
     * @return
     */
    public static UserInfo getUser(String jwtString){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(PublicConstant.JWT_SIGN_KEY)
                    .parseClaimsJws(jwtString)
                    .getBody();
            int id = Integer.parseInt(claims.getId());
            String subject = claims.getSubject();
            //校验应用名
            if(!subject.equals(PublicConstant.appName)){
                return null;
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setEmail(claims.get(userNameKey,String.class));
            userInfo.setType(claims.get(userTypeKey,Integer.class));
            return userInfo;
        }catch (Exception e){
            Console.error("checkJwt","JWT 解析失败",jwtString,e.getMessage());
            return null;
        }
    }

    /**
     * 从JWT中获取到UserId
     * @param jwtString
     * @return
     */
    public static Integer getUserId(String jwtString){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(PublicConstant.JWT_SIGN_KEY)
                    .parseClaimsJws(jwtString)
                    .getBody();
            int id = Integer.parseInt(claims.getId());
            String subject = claims.getSubject();
            //校验应用名
            if(!subject.equals(PublicConstant.appName)){
                return null;
            }
            return id;
        }catch (Exception e){
            e.printStackTrace();
            Console.error("checkJwt","JWT 解析失败",jwtString,e.getMessage());
            return null;
        }
    }

}
