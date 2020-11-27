package com.xuan.dtrun.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private static final long EXPIRE_DATE=7*24*60*60*1000;
    //token秘钥
    private static final String TOKEN_SECRET = "DTRUN";
    public static final String SALT = "XUAN";

    public static String token (String username,String password){
        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("password",password)
                    .withClaim("time",System.currentTimeMillis())
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
           throw new RuntimeException(e);
        }
        return token;
    }

    public static String md5Token(String token) {
        return DigestUtils.md5Hex(token + SALT);
    }

    public static boolean verify(String token){

        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
}
