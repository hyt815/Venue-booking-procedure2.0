package com.example.demo.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

public class JWTUtil {
    //Issuer
    public static final String ISSUER = "student.com";
    //Audience
    public static final String AUDIENCE = "Client";
    //密钥
    public static final String KEY = "ThisIsMySecretKey";
    //算法
    public static final Algorithm ALGORITHM = Algorithm.HMAC256(JWTUtil.KEY);
    //Header
    public static final Map<String, Object> HEADER_MAP = new HashMap() {
        {
            put("alg", "HS256");
            put("typ", "JWT");
        }
    };

    /**
     * 生成 Token 字符串
     *
     * @param claimMap claim 数据
     * @return Token 字符串
     */
    public static String GenerateToken(Map<String, String> claimMap) {
        Date nowDate = new Date();
        //120 分钟过期
        Date expireDate = JWTUtil.AddDate(nowDate, 60);

        //Token 建造器
        JWTCreator.Builder tokenBuilder = JWT.create();

        for (Map.Entry<String, String> entry : claimMap.entrySet()) {
            //Payload 部分，根据需求添加
            tokenBuilder.withClaim(entry.getKey(), entry.getValue());
        }

        //token 字符串

        return tokenBuilder.withHeader(JWTUtil.HEADER_MAP)//Header 部分
                .withIssuer(JWTUtil.ISSUER)//issuer
                .withAudience(JWTUtil.AUDIENCE)//audience
                .withIssuedAt(nowDate)//生效时间
                .withExpiresAt(expireDate)//过期时间
                .sign(JWTUtil.ALGORITHM);
    }

    /**
     * 时间加法
     *
     * @param date   当前时间
     * @param minute 持续时间（分钟）
     * @return 时间加法结果
     */
    private static Date AddDate(Date date, Integer minute) {
        if (null == date) {
            date = new Date();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);

        return calendar.getTime();
    }

    /**
     * 验证 Token
     *
     * @param webToken 前端传递的 Token 字符串
     * @return Token 字符串是否正确
     * @throws Exception 异常信息
     */


    public static boolean VerifyJWTToken(String webToken) throws Exception {
        String[] token = webToken.split(" ");

        if (token[0].equals("")) {
            throw new Exception("token内容为空");
        }

        //JWT验证器
        JWTVerifier verifier = JWT.require(JWTUtil.ALGORITHM).withIssuer(JWTUtil.ISSUER).build();

        //解码
        DecodedJWT jwt = verifier.verify(token[0]);//如果 token 过期，此处就会抛出异常

        //Issuer
        String issuer=jwt.getIssuer();
        if (!issuer.equals(ISSUER)){
            throw new Exception("Issuer_error");
        }

        //Audience
        List<String> audienceList = jwt.getAudience();
        String audience = audienceList.get(0);
        if (!audience.equals(AUDIENCE)){
            throw new Exception("Audience_error");
        }

        //Payload
//        Map<String, Claim> claimMap = jwt.getClaims();
//        for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
//
//        }

        //生效时间
        Date issueTime = jwt.getIssuedAt();
        //过期时间
        Date expiresTime = jwt.getExpiresAt();
        Date nowDate = new Date();
        if (nowDate.before(issueTime)||nowDate.after(expiresTime)){
            throw new Exception("token_error");
        }

        return true;
    }

}
