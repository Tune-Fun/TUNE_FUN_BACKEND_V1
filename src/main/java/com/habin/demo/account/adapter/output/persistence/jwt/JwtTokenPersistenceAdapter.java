package com.habin.demo.account.adapter.output.persistence.jwt;

import com.habin.demo.account.application.port.output.jwt.*;
import com.habin.demo.account.domain.behavior.SaveJwtToken;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.hexagon.PersistenceAdapter;
import com.habin.demo.common.property.JwtProperty;
import com.habin.demo.common.response.MessageCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Account - Output Adapter - Persistence - JwtToken
 */
@Slf4j
@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class JwtTokenPersistenceAdapter implements
        LoadUsernamePort, ValidateJwtTokenPort,
        CreateAccessTokenPort, CreateRefreshTokenPort,
        CheckAccessTokenExpirePort, CheckRefreshTokenExpirePort,
        UpdateAccessTokenExpirePort, UpdateRefreshTokenExpirePort,
        DeleteAccessTokenPort, DeleteRefreshTokenPort,
        ReissueAccessTokenPort,
        InitializingBean {

    private static final String ACCESS_TOKEN_KEY_PREFIX = "access_";
    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_";

    private SecretKey secretKey;
    private final JwtProperty jwtProperty;

    private final RedisTemplate<String, AccessToken> redisTemplateAccess;
    private final RedisTemplate<String, RefreshToken> redisTemplateRefresh;
    private final RedisTemplate<String, Object> redisTemplateObject;

    @Override
    public void afterPropertiesSet() {
        this.secretKey = hmacShaKeyFor(jwtProperty.getSecret().getBytes(UTF_8));
    }

    @Override // LoadUsernamePort
    public String loadUsernameByToken(final String token) {
        Claims body = getPayload(token);
        return body.getSubject();
    }

    @Override // ValidateJwtTokenPort
    public Boolean validate(String token) {
        try {
            getJwtParser().parseSignedClaims(token);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_INVALID_TOKEN);
        }
    }

    @Override // CreateAccessTokenPort
    public String createAccessToken(final SaveJwtToken saveJwtToken) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtProperty.getAccessTokenValidity());
        String tokenValue = buildJwts(saveJwtToken, now, expireDate);

        ValueOperations<String, AccessToken> operations = redisTemplateAccess.opsForValue();
        AccessToken buildAccessToken = new AccessToken(saveJwtToken.username(), tokenValue);

        String accessTokenKey = getAccessTokenKey(saveJwtToken.username());
        operations.set(accessTokenKey, buildAccessToken);
        redisTemplateAccess.expireAt(accessTokenKey, expireDate);

        return tokenValue;
    }

    @Override // CreateRefreshTokenPort
    public String createRefreshToken(final SaveJwtToken saveJwtToken) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtProperty.getRefreshTokenValidity());
        String tokenValue = buildJwts(saveJwtToken, now, expireDate);

        ValueOperations<String, RefreshToken> operations = redisTemplateRefresh.opsForValue();
        RefreshToken buildRefreshToken = new RefreshToken(saveJwtToken.username(), tokenValue);

        String refreshTokenKey = getRefreshTokenKey(saveJwtToken.username());
        operations.set(refreshTokenKey, buildRefreshToken);
        redisTemplateRefresh.expireAt(refreshTokenKey, expireDate);

        return tokenValue;
    }


    @Override // CheckAccessTokenExpirePort
    public Boolean isAccessTokenExpired(final String accessToken) {
        return validateJwtExpiration(accessToken, redisTemplateObject);
    }

    @Override // CheckRefreshTokenExpirePort
    public Boolean isRefreshTokenExpired(final String refreshToken) {
        return validateJwtExpiration(refreshToken, redisTemplateRefresh);
    }

    @Override // UpdateAccessTokenExpirePort
    public void updateAccessTokenExpire(final String username, final Date expireDate) {
        redisTemplateAccess.expireAt(getAccessTokenKey(username), expireDate);
    }

    @Override // UpdateRefreshTokenExpirePort
    public void updateRefreshTokenExpire(final String username, final Date expireDate) {
        redisTemplateRefresh.expireAt(getRefreshTokenKey(username), expireDate);
    }

    @Override // DeleteAccessTokenPort
    public void deleteAccessToken(final String accessToken) {
        String username = loadUsernameByTokenSkipException(accessToken);

        redisTemplateAccess.delete(ACCESS_TOKEN_KEY_PREFIX + username);

        ValueOperations<String, Object> vopObject = redisTemplateObject.opsForValue();
        vopObject.set(accessToken, true);
        redisTemplateObject.expire(accessToken, jwtProperty.getAccessTokenValidityDuration());
    }

    @Override // DeleteRefreshTokenPort
    public void deleteRefreshToken(String accessToken) {
        redisTemplateRefresh.delete(REFRESH_TOKEN_KEY_PREFIX + loadUsernameByTokenSkipException(accessToken));
    }

    @Override // ReissueAccessTokenPort
    public String reissueAccessToken(String refreshTokenValue) {
        String username = loadUsernameByToken(refreshTokenValue);
        String refreshTokenKey = getRefreshTokenKey(username);

        RefreshToken refreshToken = redisTemplateRefresh.opsForValue().get(refreshTokenKey);

        if (refreshToken == null && refreshToken.getToken().equals(refreshTokenValue))
            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_INVALID_TOKEN);

        if (isRefreshTokenExpired(refreshToken.getToken()))
            throw new CommonApplicationException(MessageCode.EXCEPTION_EXPIRED_REFRESH_TOKEN);

        SaveJwtToken behavior = new SaveJwtToken(username, getPayload(refreshTokenValue).get("role").toString());
        return createAccessToken(behavior);
    }

    private String buildJwts(SaveJwtToken saveJwtToken, Date now, Date expireDate) {
        return Jwts.builder()
                .claim("role", saveJwtToken.authorities())
                .claim("tokenType", "Bearer")
                .id(UUID.randomUUID().toString())
                .subject(saveJwtToken.username())
                .issuedAt(now)
                .notBefore(now)
                .expiration(expireDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    private String loadUsernameByTokenSkipException(String token) {
        String username = null;

        try {
            username = loadUsernameByToken(token);
        } catch (IllegalArgumentException ignored) {
        } catch (ExpiredJwtException e) {
            username = e.getClaims().getSubject(); // 만료된 access token으로부터 username를 가져옴
            log.info("username from expired access token : " + username);
        }

        return username;
    }

    private Boolean validateJwtExpiration(final String token, final RedisTemplate<String, ?> redisTemplate) {
        try {
            final Date expiration = getPayload(token).getExpiration();
            Long expireTime = redisTemplate.getExpire(token);
            return expiration.before(new Date()) || (expireTime != null && expireTime >= 0);
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Claims getPayload(final String token) {
        try {
            return getJwtParser().parseSignedClaims(token).getPayload();
        } catch (IllegalArgumentException e) {
            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_INVALID_TOKEN);
        }
    }

    private JwtParser getJwtParser() {
        return Jwts.parser().verifyWith(secretKey)
                .sig().add(Jwts.SIG.HS512)
                .and()
                .build();
    }

    private static String getAccessTokenKey(final String username) {
        return ACCESS_TOKEN_KEY_PREFIX + username;
    }

    private static String getRefreshTokenKey(final String username) {
        return REFRESH_TOKEN_KEY_PREFIX + username;
    }
}
