package com.it.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.it.entity.ApiResult;
import com.it.enumration.ResponseCodeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HSL
 * @date 2021-11-22 22:18
 * @desc token认证
 */
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    /**
     * token过期秒数
     */
    @Value("${token.expire.seconds}")
    private Integer expireSeconds;
    /**
     * 私钥
     */
    @Value("${token.jwtSecret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        String uri = serverHttpRequest.getURI().getPath();
        //  检查白名单（配置）
        if (uri.contains("/login")) {
            return chain.filter(exchange);
        }
        String token = serverHttpRequest.getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_MISSION);
        }
        String userId;
        try {
            // 解析token
            // 校验token是否过期
            Claims claims = this.getClaimsFromToken(token);
            userId = MapUtils.getString(claims, "userId");
            userId = StrUtil.isBlank(userId) ? "0" : userId;
            Long expireTime = MapUtils.getLong(claims, "expireTime");
            if (expireTime > System.currentTimeMillis()) {
                // 2004
                return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_EXPIRED);
            }
        } catch (Exception ex) {
            return getVoidMono(serverHttpResponse, ResponseCodeEnum.UNKNOWN_ERROR);
        }
        ServerHttpRequest mutableReq = serverHttpRequest.mutate().header("userId", userId).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, ResponseCodeEnum responseCodeEnum) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        ApiResult result = ApiResult.fail(String.valueOf(responseCodeEnum.getCode()), responseCodeEnum.getMessage());
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(result).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

//    private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    /**
     * @author HSL
     * @date 2021-11-21
     * @desc 从token中获取其唯一标识
     **/
//    private String getUUIDFromJWT(String jwt) {
//        if ("null".equals(jwt) || StringUtils.isBlank(jwt)) {
//            return null;
//        }
//        Map<String, Object> jwtClaims;
//        try {
//            // jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
//            jwtClaims = this.getClaimsFromToken(jwt);
//            return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
//        } catch (ExpiredJwtException e) {
//            log.error("{}已过期", jwt);
//        } catch (Exception e) {
//            log.error("{%s}", e);
//        }
//        return null;
//    }

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

    /**
     * @author HSL
     * @date 2021-11-22
     * @desc 校验token
     **/
//    public static void verifyToken(String token, String secretKey) {
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(secretKey);
//            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
//            jwtVerifier.verify(token);
//        } catch (JWTDecodeException jwtDecodeException) {
//            throw new TokenAuthenticationException(ResponseCodeEnum.TOKEN_INVALID.getCode(), ResponseCodeEnum.TOKEN_INVALID.getMessage());
//        } catch (SignatureVerificationException signatureVerificationException) {
//            throw new TokenAuthenticationException(ResponseCodeEnum.TOKEN_SIGNATURE_INVALID.getCode(), ResponseCodeEnum.TOKEN_SIGNATURE_INVALID.getMessage());
//        } catch (TokenExpiredException tokenExpiredException) {
//            throw new TokenAuthenticationException(ResponseCodeEnum.TOKEN_EXPIRED.getCode(), ResponseCodeEnum.TOKEN_INVALID.getMessage());
//        } catch (Exception ex) {
//            throw new TokenAuthenticationException(ResponseCodeEnum.UNKNOWN_ERROR.getCode(), ResponseCodeEnum.UNKNOWN_ERROR.getMessage());
//        }
//    }
    @Override
    public int getOrder() {
        return -100;
    }
}
