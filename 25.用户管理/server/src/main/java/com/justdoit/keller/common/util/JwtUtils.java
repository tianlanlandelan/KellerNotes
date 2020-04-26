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

    private static String typeKey = "type";

    private static String codeKey = "code";

    /**
     * 获取登录的 JWT
     * @param userInfo
     * @return
     */
    public static String getJwtForLogin(UserInfo userInfo){
        return getJwtString(userInfo,PublicConstant.LOGIN_TYPE,null);
    }

    public static String getJwtForResetPassword(UserInfo userInfo,String code){
        return getJwtString(userInfo,PublicConstant.RESET_PASSWORD_TYPE,code);
    }


    /**
     * 生成 JWT
     * 如果是重置密码的JWT，其中包含邮件验证码
     * @param userInfo 用户信息
     * @param type JWT 类型
     * @return
     */
    private static String getJwtString(UserInfo userInfo,int type,String code){
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
                .claim(typeKey,type)
                //签名 密钥
                .signWith( SignatureAlgorithm.HS256, PublicConstant.JWT_SIGN_KEY);
        if(type == PublicConstant.RESET_PASSWORD_TYPE){
            jwtBuilder.claim(codeKey,code);
        }
        return jwtBuilder.compact();
    }

    private static Claims getClaims(String jwtString,int type){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(PublicConstant.JWT_SIGN_KEY)
                    .parseClaimsJws(jwtString)
                    .getBody();
            String subject = claims.getSubject();
            //校验应用名
            if(!subject.equals(PublicConstant.appName)){
                return null;
            }
            //校验 JWT 类型
            int jwtType = claims.get(typeKey,Integer.class);
            if(jwtType != type){
                return null;
            }
            return claims;
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
    public static UserInfo getUserFromLogin(String jwtString){
        Claims claims = getClaims(jwtString,PublicConstant.LOGIN_TYPE);
        if(claims == null){
            return null;
        }
        UserInfo userInfo = new UserInfo(Integer.parseInt(claims.getId()));
        userInfo.setEmail(claims.get(userNameKey,String.class));
        userInfo.setType(claims.get(userTypeKey,Integer.class));
        return userInfo;
    }

    /**
     * 从JWT中获取到email
     * @param jwtString
     * @return
     */
    public static String getEmailForResetPassword(String jwtString){
        Claims claims = getClaims(jwtString,PublicConstant.RESET_PASSWORD_TYPE);
        if(claims == null){
            return null;
        }
        return claims.get(userNameKey,String.class);
    }
    public static String getCodeForResetPassword(String jwtString){
        Claims claims = getClaims(jwtString,PublicConstant.RESET_PASSWORD_TYPE);
        if(claims == null){
            return null;
        }
        return claims.get(codeKey,String.class);
    }

    public static Integer getUserTypeForResetPassword(String jwtString){
        Claims claims = getClaims(jwtString,PublicConstant.RESET_PASSWORD_TYPE);
        if(claims == null){
            return null;
        }
        return claims.get(userTypeKey,Integer.class);
    }


}
