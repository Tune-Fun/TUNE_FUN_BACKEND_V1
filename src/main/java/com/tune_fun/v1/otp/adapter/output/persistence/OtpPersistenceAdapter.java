package com.tune_fun.v1.otp.adapter.output.persistence;

import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.otp.application.port.output.SaveOtpPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@PersistenceAdapter
@RequiredArgsConstructor
public class OtpPersistenceAdapter implements SaveOtpPort {

    private final RedisTemplate<String, OtpRedisEntity> redisTemplate;

}
