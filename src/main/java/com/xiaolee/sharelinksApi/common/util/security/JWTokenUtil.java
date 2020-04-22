package com.xiaolee.sharelinksApi.common.util.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTokenUtil {
    private static final String key = "_xiaobaba123*@+-#";
    private static final String alg = "HS256";
    private static final String typ = "JWT";
    private static final String iss = "web.reeveslee.com";
    private static final String sub = "UserLoginToken";
    private String aud; // 授权用户名
    private Long exp; // 过期时间
    private Long iat; // 授权时间
    private String token=""; // 授权token
    private JsonParser jsonParser;
    private ObjectMapper objectMapper;


    public JWTokenUtil(String aud, int exp) {
        Long now = new Date().getTime();
        this.aud = aud;
        this.exp = now + exp;
        this.iat = now;
        this.jsonParser = JsonParserFactory.getJsonParser();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 获取登陆token
     *
     * @return
     */
    public String getToken() {
        if (!"".equals(this.token)) {
            return this.token;
        }

        Map<String, Object> head = new HashMap<>();
        head.put("alg", alg);
        head.put("typ", typ);
        Map<String, Object> payload = new HashMap<>();
        payload.put("iss", iss);
        payload.put("exp", this.exp);
        payload.put("sub", sub);
        payload.put("aud", this.aud);
        payload.put("iat", this.iat);

        try {
            String headBase64Str = new String(Base64.encodeBase64(this.objectMapper.writeValueAsString(head).getBytes()));
            String payloadBase64Str = new String(Base64.encodeBase64(this.objectMapper.writeValueAsString(payload).getBytes()));
            String signature = HMACSHA256(headBase64Str + "." + payloadBase64Str);
            this.token = headBase64Str + '.' + payloadBase64Str + "." + signature;
            return this.token;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * HMACSHA256签名
     *
     * @param data
     * @return
     */
    public String HMACSHA256(String data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString();
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public static String getAlg() {
        return alg;
    }

    public static String getTyp() {
        return typ;
    }

    public static String getIss() {
        return iss;
    }

    public static String getSub() {
        return sub;
    }
}
