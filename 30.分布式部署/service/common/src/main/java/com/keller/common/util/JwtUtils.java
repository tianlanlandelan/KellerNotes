package com.keller.common.util;

import com.keller.common.config.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
 * @author yangkaile
 * @date 2020-03-05 17:05:11
 *
 * Jwt 工具类，用于生成及解析JWT
 */
public class JwtUtils {

    public static final String ID = "id";

    public static final String EMAIL = "email";

    public static final String USER_TYPE = "userType";

    public static final String JWT_TYPE = "jwtType";

    public static final String CODE = "code";

    public static final String AUTHOR = "YangKaiLe";

    /**
     * JWT 签名
     */
    public static final String JWT_SIGN_KEY = "KellerNotes!@#$%^&*()_+kellernotes!@#$%^&*()_+Kellernotes!@#$%^&*()_+kellerNotes";

    private static Key getKey() {
        byte[] bytes = JWT_SIGN_KEY.getBytes();
        //用Base64解码可以获取Key对应的字符串
        return Keys.hmacShaKeyFor(bytes);
    }

    public static void main(String[] args){
        Console.print("getJwtString",getJwtString(1,"email",1,1,"code"));
    }

    public static String getJwtString(int id,String email,int userType,int jwtType,String code){
        long now=System.currentTimeMillis();
        if(ObjectUtils.isEmpty(code)){
            code = CODE;
        }
        //生成token
        return Jwts.builder()
                // 用户 ID
                .setId(id + "")
                // 发行人 用于校验
                .setIssuer(AUTHOR)
                // 主题 用于校验
                .setSubject(Constants.appName)
                // 用户
                .setAudience(email)
                //过期时间
                .setExpiration( new Date( now + Constants.JWT_EXP_TIME ) )
                //自定义内容
                .claim(USER_TYPE,userType)
                .claim(JWT_TYPE,jwtType)
                .claim(CODE,code)
                .signWith(getKey())
                .compact();
    }

    /**
     * 从JWT中获取到UserId
     * @param token
     * @return
     */
    public static HashMap<String,String> readJwt(String token){
       Claims claims = Jwts.parserBuilder()
               .setSigningKey(getKey())
               .build().parseClaimsJws(token).getBody();

       //校验应用名
       if(!Constants.appName.equals(claims.getSubject())){
           return null;
       }
        //校验应用名
        if(!AUTHOR.equals(claims.getIssuer())){
            return null;
        }
        HashMap<String,String> map = new HashMap(16);
        map.put(ID,claims.getId());
        map.put(EMAIL,claims.getAudience());
        map.put(USER_TYPE,claims.get(USER_TYPE,Integer.class) + "");
        map.put(JWT_TYPE,claims.get(JWT_TYPE,Integer.class) + "");
        map.put(CODE,claims.get(CODE,String.class));
        return map;
    }




}
