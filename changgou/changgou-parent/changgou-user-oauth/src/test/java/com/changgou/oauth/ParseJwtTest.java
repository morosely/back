package com.changgou.oauth;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.c97v07pjsKwzVaRddl_VQBLrtzTEBhSsTzQ4PDyQMh9555as2Rovre5an18ErmkyjdRiRqHaeGOE1g9SB5-ajZYipwSIuwQiOJxOyr9sYesEb3oon8zgQ0J-SR3vaWCgq88-1LCHZxwRTUK_iKUrn3Z10nKW7fJ6YBqEHg6az0rn62Xzzr_J1jpYwXRqBZ-jRt4t5fdM-zDHrXZjRwuTmL9KHkjpfA6KpYSo1Wv7GK8H-c5X9V4Xi00QxuvMFrqIwyMvjQQWw8xi0G1BH-omgBgmWYXTCzUCPNnESaannDpsyIj9uyrTwVUmMhULG8_43zrpkMZ3kDt-xbd9hSPUbQ";
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKZdpaHOlJtG0qUVu8haUBGD4f7N2hzBQdnBIeMO7Eyc6sHqg2Ro85ig7qbZgqOVzi6twxae6lTAPSRJJ8sUD/6Yxkp4Lpkr8FdPxD/hYphmOMb/i7tRF3dzlvBLHgMjDGtNn2mkwV4tTdud/Pv+RnzGIzS3fUUJzN3xnXxsaEYOZi9UitdpyOzok1rv41rjBzIKvMVEzLpx0HN5lrPQjAWuLUG126rM5+uuFiNIsq3cn5RQ4nPbT+A8egjIb6ZvbbrKG+0GVtkGziE6fAXmTJVZ93VZenPSGqgCYFPkhmHNX2+kbb5VW/MjkEShkTlj3eFhpofjnwJjFAbuVU0oFQIDAQAB-----END PUBLIC KEY-----";
        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
