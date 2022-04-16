package com.lxs.cloud;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestJWT {

    @Test
    public void createJWT() {
        long l1 = System.currentTimeMillis(); //当前时间戳
        long l2 = l1 + 20000; //过期时间错
        JwtBuilder builder = Jwts.builder()
                .setId("12356") //设置唯一编号
                .setSubject("password123")  //主题
                .setIssuedAt(new Date()) //签发时间
//                .setExpiration(new Date(l2))
                .claim("role", "ADMIN")
                .signWith(SignatureAlgorithm.HS256, "kaikeba"); //签名，采用HS256算法加密，秘钥
        //自定义载荷
        Map<String, Object> map = new HashMap<>();
        map.put("myaddress", "cn");
        map.put("mycity", "hangzhou");
        builder.addClaims(map);

        System.out.println(builder.compact());
    }

    @Test
    public void testParseJwt() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM1NiIsInN1YiI6InBhc3N3b3JkMTIzIiwiaWF0IjoxNjA1NzY4NzQ4LCJyb2xlIjoiQURNSU4iLCJteWNpdHkiOiJoYW5nemhvdSIsIm15YWRkcmVzcyI6ImNuIn0.598OCjR-ItxRat7_VvWklUbIgGehJuEcXu21EEub8vs";
        Claims claims = Jwts.parser()
                .setSigningKey("kaikeba")
                .parseClaimsJws(jwt)
                .getBody();
        System.out.println(claims);

        System.out.println(claims.get("role"));
        System.out.println(claims.getSubject());
    }

}
