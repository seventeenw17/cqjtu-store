package com.backend.springstore.security;

import com.backend.springstore.common.ServiceCode;
import com.backend.springstore.common.ServiceException;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    // 设置token的有效期
    // 类比记住密码与重新登录
    private static final long TOKEN_EXPIRE = 14 * 24 * 60 * 60 * 1000;
    // 设置一个密钥私钥,为了安全需要定期更换
    private static final String TOKEN_SECRET = "114514cqjtu_123_%$@^YVhh";

    /**
     * 生成token的方法
     * 此时的token为空
     */
    public static String generateToken(UserLoginVO user){
        Map<String,Object> map = new HashMap<>();
        // 主体
        String subject = user.getUsername();
        // 当前时间
        Date now = new Date();
        // 租约到期时间
        long expireTime = now.getTime() + TOKEN_EXPIRE;
        // 创建过期时间
        Date exp = new Date(expireTime);
        // 生成token
        String token = Jwts.builder().setSubject(subject) // 设置主体
                .setIssuedAt(now) // 设置当前时间
                .setExpiration(exp) // 设置过期时间
                .claim("id", user.getId()) // id
                .claim("username",user.getUsername()) // 用户名称
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET) // 算法+签名
                .compact();
        return token;
    }

    /**
     * 校验token
     */
    public static Claims verifyToken(String token) {
        // 校验token
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(TOKEN_SECRET) // 设置签名
                    .parseClaimsJws(token) // 设置解析的token
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ServiceException(ServiceCode.ERROR_JWT_EXPIRED, "令牌过期");
        } catch (SignatureException e) {
            throw new ServiceException(ServiceCode.ERROR_JWT_SIGNATURE, "令牌无效");
        } catch (Exception e) {
            throw new ServiceException(ServiceCode.ERROR_JWT_MALFORMED, "令牌非法");
        }
        return claims;
    }

    /**
     * 檢查token的合法性
     */
    public static Claims parseToken(String token) {
        Claims claims = verifyToken(token);
        try {
            claims = Jwts.parser().setSigningKey(TOKEN_SECRET) // 设置签名
                    .parseClaimsJws(token) // 设置解析的token
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ServiceException(ServiceCode.ERROR_JWT_EXPIRED,"令牌过期");
        } catch (SignatureException e) {
            throw new ServiceException(ServiceCode.ERROR_JWT_SIGNATURE,"令牌无效");
        } catch (Exception e) {
            throw new ServiceException(ServiceCode.ERROR_JWT_MALFORMED,"令牌非法");
        }
        return claims;
    }
    /**
     * 从token中获取用户信息
     */
    public static UserLoginVO getUserInfo(String token){
        //校验token
        Claims claims = parseToken(token);
        //获取id
        Integer id = Integer.valueOf(claims.get("id").toString());
        //获取用户名称
        String username = claims.get("username").toString();
        UserLoginVO user = new UserLoginVO();
        user.setId(id);
        user.setUsername(username);
        return user;
    }
}

