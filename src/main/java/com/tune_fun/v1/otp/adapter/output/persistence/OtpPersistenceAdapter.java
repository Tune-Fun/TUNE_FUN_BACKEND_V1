package com.tune_fun.v1.otp.adapter.output.persistence;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.common.util.EncryptUtil;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.otp.application.port.output.LoadOtpPort;
import com.tune_fun.v1.otp.application.port.output.SaveOtpPort;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import com.tune_fun.v1.otp.domain.behavior.LoadOtp;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.state.CurrentDecryptedOtp;
import com.tune_fun.v1.otp.domain.state.CurrentOtp;
import com.tune_fun.v1.otp.domain.behavior.SaveOtp;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

import static com.tune_fun.v1.common.response.MessageCode.*;
import static java.lang.String.format;

@PersistenceAdapter
@RequiredArgsConstructor
public class OtpPersistenceAdapter implements SaveOtpPort, LoadOtpPort, VerifyOtpPort {

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

    @Override
    public CurrentDecryptedOtp loadOtp(LoadOtp loadOtp) throws Exception {
        ValueOperations<String, OtpRedisEntity> ops = redisTemplate.opsForValue();
        OtpRedisEntity otpRedisEntity = ops.get(setOtpKey(loadOtp.otpType(), loadOtp.username()));
        String decrypted = encryptUtil.decrypt(otpRedisEntity.getToken());
        return new CurrentDecryptedOtp(loadOtp.username(), loadOtp.otpType(), decrypted);
    }

    @Override
    public void verifyOtp(VerifyOtp verifyOtp) throws Exception {
        String otpKey = setOtpKey(verifyOtp.otpType(), verifyOtp.username());
        ValueOperations<String, OtpRedisEntity> ops = redisTemplate.opsForValue();
        OtpRedisEntity otpRedisEntity = ops.get(otpKey);

        if (otpRedisEntity == null) throw new CommonApplicationException(EXCEPTION_OTP_NOT_FOUND);

        if(checkRedisExpiration(ops, otpKey))
            throw new CommonApplicationException(EXCEPTION_OTP_EXPIRED);

        if (!checkMatchValue(verifyOtp.otp(), otpRedisEntity))
            throw new CommonApplicationException(EXCEPTION_OTP_NOT_MATCH);

        redisTemplate.delete(otpKey);
    }

    private Boolean checkRedisExpiration(final ValueOperations<String, OtpRedisEntity> ops, final String key) {
        return ops.get(key) == null || redisTemplate.getExpire(key) == null || redisTemplate.getExpire(key) < 0;
    }

    private Boolean checkMatchValue(@NotNull String value, @NotNull OtpRedisEntity otpRedisEntity) throws Exception {
        return Objects.equals(value, encryptUtil.decrypt(otpRedisEntity.getToken()));
    }

    private String setOtpKey(final OtpType otpType, final String username) {
        return format("otp::%s::%s", otpType, username);
    }
}
