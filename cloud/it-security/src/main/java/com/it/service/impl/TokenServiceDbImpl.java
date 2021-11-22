package com.it.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.it.dao.TokenDao;
import com.it.dto.LoginUser;
import com.it.dto.Token;
import com.it.entity.TokenModel;
import com.it.service.SysLogService;
import com.it.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author HSL
 * @date 2021-11-15 0:43
 * @desc token存到数据库的实现类
 */
@Service("tokenService")
@Slf4j
public class TokenServiceDbImpl implements TokenService {

    /**
     * token过期秒数
     */
    @Value("${token.expire.seconds}")
    private Integer expireSeconds;
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private SysLogService logService;
    /**
     * 私钥
     */
    @Value("${token.jwtSecret}")
    private String jwtSecret;

    private static Key KEY = null;
    private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    @Override
    public Token saveToken(LoginUser loginUser) {
        loginUser.setToken(UUID.randomUUID().toString());
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
        TokenModel model = new TokenModel();
        model.setId(loginUser.getToken());
        model.setCreateTime(new Date());
        model.setUpdateTime(new Date());
        model.setExpireTime(new Date(loginUser.getExpireTime()));
        model.setVal(JSONObject.toJSONString(loginUser));
        tokenDao.save(model);
        // 登录日志
        logService.save(loginUser.getId(), "登录", true, null);
        String jwtToken = createJWTToken(loginUser, loginUser.getExpireTime());
        return new Token(jwtToken, loginUser.getLoginTime());
    }

    /**
     * @author HSL
     * @date 2021-11-15
     * @desc 生成jwt
     **/
    private String createJWTToken(LoginUser loginUser, Long expireTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUser.getId());
        claims.put("expireTime", expireTime);
        claims.put(LOGIN_USER_KEY, loginUser.getToken());// 放入一个随机字符串，通过该串可找到登陆用户
        return this.generateToken(claims);
        /*return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();*/
    }

    @Override
    public void refresh(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
        TokenModel model = tokenDao.getById(loginUser.getToken());
        model.setUpdateTime(new Date());
        model.setExpireTime(new Date(loginUser.getExpireTime()));
        model.setVal(JSONObject.toJSONString(loginUser));
        tokenDao.update(model);
    }

    @Override
    public LoginUser getLoginUser(String jwtToken) {
        String uuid = getUUIDFromJWT(jwtToken);
        if (uuid != null) {
            TokenModel model = tokenDao.getById(uuid);
            return toLoginUser(model);
        }
        return null;
    }

    @Override
    public boolean deleteToken(String jwtToken) {
        String uuid = getUUIDFromJWT(jwtToken);
        if (uuid != null) {
            TokenModel model = tokenDao.getById(uuid);
            LoginUser loginUser = toLoginUser(model);
            if (loginUser != null) {
                tokenDao.delete(uuid);
                logService.save(loginUser.getId(), "退出", true, null);
                return true;
            }
        }

        return false;
    }

    private LoginUser toLoginUser(TokenModel model) {
        if (model == null) {
            return null;
        }
        // 校验是否已过期
        if (model.getExpireTime().getTime() > System.currentTimeMillis()) {
            return JSONObject.parseObject(model.getVal(), LoginUser.class);
        }
        return null;
    }

    private Key getKeyInstance() {
        if (KEY == null) {
            synchronized (TokenServiceDbImpl.class) {
                if (KEY == null) {// 双重锁
                    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
                    KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
                }
            }
        }
        return KEY;
    }

    /**
     * @author HSL
     * @date 2021-11-21
     * @desc 从token中获取其唯一标识
     **/
    private String getUUIDFromJWT(String jwt) {
        if ("null".equals(jwt) || StringUtils.isBlank(jwt)) {
            return null;
        }
        Map<String, Object> jwtClaims;
        try {
            // jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
            jwtClaims = this.getClaimsFromToken(jwt);
            return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
        } catch (ExpiredJwtException e) {
            log.error("{}已过期", jwt);
        } catch (Exception e) {
            log.error("{%s}", e);
        }
        return null;
    }

    /**
     * @author HSL
     * @date 2021-11-21
     * @desc 生成token
     **/
    public String generateToken(Map<String, Object> claims) {
        Date createdTime = new Date();
        Date expirationTime = getExpirationTime();
        byte[] keyBytes = this.jwtSecret.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdTime)
                .setExpiration(expirationTime)
                // 你也可以改用你喜欢的算法
                // 支持的算法详见：https://github.com/jwtk/jjwt#features
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * @author HSL
     * @date 2021-11-21
     * @desc token过期时间
     **/
    public Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + this.expireSeconds * 1000);
    }

    /**
     * @author HSL
     * @date 2021-11-21
     * @desc 从token中获取claim
     **/
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("token解析错误", e);
            throw new IllegalArgumentException("Token invalided.");
        }
    }
}
