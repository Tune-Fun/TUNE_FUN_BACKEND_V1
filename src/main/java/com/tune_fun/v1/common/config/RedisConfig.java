package com.tune_fun.v1.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tune_fun.v1.account.adapter.output.persistence.jwt.AccessTokenRedisEntity;
import com.tune_fun.v1.account.adapter.output.persistence.jwt.RefreshTokenRedisEntity;
import com.tune_fun.v1.common.constant.Constants;
import com.tune_fun.v1.common.util.count.AtomicCounter;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpRedisEntity;
import com.tune_fun.v1.vote.adapter.output.persistence.VoteCountRedisEntity;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.time.Duration.ofDays;
import static java.time.Duration.ofHours;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Slf4j
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisConfiguration redisConfiguration = LettuceConnectionFactory.createRedisConfiguration(redisURI());
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create(redisURI());
    }

    private RedisURI redisURI() {
        return RedisURI.builder()
                .withHost(redisProperties.getHost())
                .withPort(redisProperties.getPort())
                .withDatabase(redisProperties.getDatabase())
                .withAuthentication(
                        redisProperties.getUsername() != null ? redisProperties.getUsername() : "",
                        redisProperties.getPassword() != null ? redisProperties.getPassword() : "")
                .build();
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .prefixCacheNameWith("Cache_")
                .entryTtl(Duration.ofMinutes(30));

        Map<String, RedisCacheConfiguration> redisCacheConfigMap = new HashMap<>();
        redisCacheConfigMap.put(Constants.CacheNames.VOTE_PAPER, redisCacheConfiguration.entryTtl(ofHours(1)));
        redisCacheConfigMap.put(Constants.CacheNames.VOTE_CHOICE, redisCacheConfiguration.entryTtl(ofDays(1)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(redisCacheConfigMap)
                .build();
    }

    @Bean
    public RedisTemplate<String, AccessTokenRedisEntity> redisTemplateForAccessToken() {
        RedisTemplate<String, AccessTokenRedisEntity> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(AccessTokenRedisEntity.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, RefreshTokenRedisEntity> redisTemplateForRefreshToken() {
        RedisTemplate<String, RefreshTokenRedisEntity> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshTokenRedisEntity.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, OtpRedisEntity> redisTemplateForOtp() {
        RedisTemplate<String, OtpRedisEntity> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(OtpRedisEntity.class));
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForObject() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Long> redisTemplateForLong() {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return redisTemplate;
    }
}
