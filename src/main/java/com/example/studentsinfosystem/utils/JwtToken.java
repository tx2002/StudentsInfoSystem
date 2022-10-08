package com.example.studentsinfosystem.utils;
import com.example.studentsinfosystem.entity.Account;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 *
 * @author TX
 * @date 2022/10/5 19:26
 */
@Configuration
public class JwtToken {

    private long time = 1000*60*60*2;//两小时过期
    private String signature = "txtx";//设置密钥

    public String jwt(Account accout){
        //Token组成三部分,header,payload,signature
        String Token = Jwts.builder()
                //header
                .setHeaderParam("typ", "JWT")
                //.setHeaderParam("alg","HS256"),默认的算法签名,可以不写
                //payload,里面会有一些主要内容
                .claim("username", accout.getUsername())//设置username
                .claim("role", accout.getRole())//设置role
                .setExpiration(new Date(System.currentTimeMillis() + time))//设置到期时间
                .setIssuedAt(new Date(System.currentTimeMillis()))//创建时间
                //signature
                .signWith(SignatureAlgorithm.HS256, signature)//算法加密,唯一性的特征
                .compact();//拼接
        return Token;
    }

    //根据Token读取关键信息username和role
    public Claims getClaimByToken(String token) {
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body;
    }
    /**
     * token是否过期
     * @return  true：过期
     */
    public static boolean isTokenExpired(Date expiration) {
        return new Date().before(expiration);
    }
}
