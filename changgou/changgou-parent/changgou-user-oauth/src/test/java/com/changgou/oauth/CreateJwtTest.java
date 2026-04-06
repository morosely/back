package com.changgou.oauth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class CreateJwtTest {

    /***
     * 创建令牌测试
     */
    @Test
    public void testCreateToken(){
        //证书文件路径
        String key_location= "omdmain.jks";
        //秘钥库密码
        String key_password="omdmain";
        //秘钥密码
        String keypwd = "omdmain";
        //秘钥别名
        String alias = "omdmain";

        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        //创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,key_password.toCharArray());

        //读取秘钥对(公钥、私钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypwd.toCharArray());

        //获取私钥
        RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();

        //定义Payload
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "1");
        tokenMap.put("name", "itheima");
        tokenMap.put("roles", "ROLE_VIP,ROLE_USER");
        tokenMap.put("authorities", new String[]{"admin","oauth"});

        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate));

        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //注：每次加密后的值都是不一样的
        System.err.println("omdmain : "+passwordEncoder.encode("omdmain"));
        System.err.println("1qaz@wsx : "+passwordEncoder.encode("1qaz@wsx"));
        //System.err.println(BCrypt.checkpw("changgou","$2a$10$wZRCFgWnwABfE60igAkBPeuGFuzk74V2jw3/trkdUZpnteCtJ9p9m"));
    }
}
