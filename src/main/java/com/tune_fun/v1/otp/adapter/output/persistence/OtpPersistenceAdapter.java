package com.tune_fun.v1.otp.adapter.output.persistence;

import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.common.util.EncryptUtil;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.otp.application.port.output.SaveOtpPort;
import com.tune_fun.v1.otp.domain.state.CurrentOtp;
import com.tune_fun.v1.otp.domain.behavior.SaveOtp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Date;

import static java.lang.String.format;

@PersistenceAdapter
@RequiredArgsConstructor
public class OtpPersistenceAdapter implements SaveOtpPort {

    private final RedisTemplate<String, OtpRedisEntity> redisTemplate;
    private final EncryptUtil encryptUtil;

    @Value("${otp.validity}")
    private Duration otpValidity;

    @Override
    public CurrentOtp saveOtp(final SaveOtp saveOtp) throws Exception {
        String token = StringUtil.randomNumeric(6);
        String otpKey = setOtpKey(saveOtp.otpType(), saveOtp.username());
        String encryptedToken = encryptUtil.encrypt(token);

        OtpRedisEntity otpRedisEntity = new OtpRedisEntity(saveOtp.username(), encryptedToken);

        ValueOperations<String, OtpRedisEntity> ops = redisTemplate.opsForValue();
        ops.set(otpKey, otpRedisEntity);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + otpValidity.toMillis());
        redisTemplate.expireAt(otpKey, expireDate);

        return new CurrentOtp(saveOtp.username(), OtpType.FORGOT_PASSWORD, encryptedToken);
    }

    private String setOtpKey(final OtpType otpType, final String username) {
        return format("otp::%s::%s", otpType, username);
    }

}
