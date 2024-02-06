package com.habin.demo.common.config;

import com.habin.demo.common.rate.RatePlan;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import static io.github.bucket4j.distributed.ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax;
import static java.time.Duration.ofSeconds;

@Configuration
@DependsOn("redisConfig")
@RequiredArgsConstructor
public class RateLimiterConfig {

    private final RedisClient redisClient;

    @Bean
    public LettuceBasedProxyManager<byte[]> lettuceBasedProxyManager() {
        return LettuceBasedProxyManager
                .builderFor(redisClient)
                .withExpirationStrategy(basedOnTimeForRefillingBucketUpToMax(ofSeconds(10)))
                .build();
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(RatePlan.TEST.getLimit())
                .build();
    }

}
