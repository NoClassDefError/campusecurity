package cn.macswelle.campusecurity.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    public static final long TOKEN_EXPIRE_TIME = 7200 * 1000;

    /**
     * 生成Token
     */
    public static String generateToken(String username, String secretKey) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        String token = JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(expireTime)
                .withClaim("content", username)
                .sign(algorithm);

        return token;
    }

    /**
     * 校验Token
     */
    public static boolean verifyToken(String token, String secretKey) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
            return true;
        } catch (Exception ignored){
          return false;
        }
    }

    /**
     * 从Token中提取用户信息
     */
    public static String getUserInfo(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("content").asString();
    }

}
