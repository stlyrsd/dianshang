package com.service.auth.serviceauth;

import java.io.IOException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTTest {

    @Test
    public void testGenPassword() {
//        System.out.println(new BCryptPasswordEncoder().encode("abcdefg"));
        System.out.println(new BCryptPasswordEncoder().matches("1234562", "$2a$10$cqDiBXGunn/GcEeKvZX62eABaQyjyf3eJEW7AIL98uiq8UM.VZZfi"));
    }

    //生成一个jwt令牌
    @Test
    public void testCreateJwt() throws Exception {
        //证书文件
        String key_location = "kaikeba.jks";
        //密钥库密码
        String keystore_password = "kaikeba";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, keystore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "kaikeba";
        //密钥别名
        String alias = "kaikeba";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        tokenMap.put("id", "123");
        tokenMap.put("name", "mrt");
        tokenMap.put("roles", "r01,r02");
        tokenMap.put("ext", "1");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(new ObjectMapper().writeValueAsString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token=" + token);
    }

    //资源服务使用公钥验证jwt的合法性，并对jwt解码
    @Test
    public void testVerify() {
        //jwt令牌
        String token
                = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDY5MDYwMDksInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9NQU5BR0VSIiwiUk9MRV9BRE1JTiJdLCJqdGkiOiI3NWQyNzU2MS04ZGMwLTQzZTYtYmYwYi1lYmNkYWRkMTM3MDEiLCJjbGllbnRfaWQiOiJjbGllbnQiLCJzY29wZSI6WyJyZWFkIl19.Pqr3fp7AO1fZPxTLY24wXnSUyyTpJKQJsBHauGYyKfNG77CtYCPFc9n5QsS8Hka3vsIXbycACfCNnMYUIYanfSrPPydttxOPzyexthLIjwcXjoThgFjdTondlVKcNJOJC5LUG7_2YmB0A3RB_bw8RIFrQrW6JN-pHHfTfzc4JZhcr1vycOkjsvWNvvYa2WtuSdB2MA5b58OoRg_I-gVPCXn_d2_Zzqk34pc9skX_8FW1StBI5TjWn7eLjhTyszOu8-661DX6AVtPiEOV8EIUypjjcshMfCfvWh4Zgtaw-7DDVbUbM8WremT96DK_s2CuPWkFlPsc-S7x3b2YkDySOg";
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwtYpjt7NtpS1B51x6PUK7ryvKySK4VQi7KUCGBm6kisErNM+FwdgKMbpQxTtWoYyXfQsWwuhBW45+uF+Z5DUDaLtHlMV55eA5fkGLFZ1F9ppZC+2Etsy1CyPqA0Mx8R0/HbMB1no4KTlQpqST7JjCdtwLWqUd68zDlfToIsWB1fHuYHbH/DCGUBmZb+16805/SjWkYvj3B6F+WJ8Gm47/OJBH+wo7k4GWZ7OXdMcNnYWMyBfa4abjo7cxjoHL2fDanS6And4Sh3cZEJde4WgXsEktvR/EaZR7CeQzwzOg47+5cCcFSYgmVfpDyLsBnFkG3WFs/qZ3yPzy+DQKLIF2wIDAQAB-----END PUBLIC KEY-----";
        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);

        try {
            Map<String, String> map = new ObjectMapper().readValue(claims, Map.class);
            System.out.println(map.get("user_name"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //生成一个jwt令牌
    @Test
    public void testCreateAdminJwt() throws Exception {
        //证书文件
        String key_location = "kaikeba.jks";
        //密钥库密码
        String keystore_password = "kaikeba";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, keystore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "kaikeba";
        //密钥别名
        String alias = "kaikeba";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        tokenMap.put("user_name", "admin");
        tokenMap.put("authorities", new String[] {"ROLE_ADMIN"});
        tokenMap.put("client_id", "client");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(new ObjectMapper().writeValueAsString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token=" + token);
    }



}
