package com.tune_fun.v1.otp.adapter.output.persistence;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.common.util.EncryptUtil;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.otp.application.port.output.DeleteOtpPort;
import com.tune_fun.v1.otp.application.port.output.LoadOtpPort;
import com.tune_fun.v1.otp.application.port.output.SaveOtpPort;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import com.tune_fun.v1.otp.domain.behavior.LoadOtp;
import com.tune_fun.v1.otp.domain.behavior.SaveOtp;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.state.CurrentDecryptedOtp;
import com.tune_fun.v1.otp.domain.state.CurrentOtp;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

import static com.tune_fun.v1.common.response.MessageCode.*;
import static com.tune_fun.v1.otp.adapter.output.persistence.OtpType.fromLabel;
import static java.lang.String.format;


@PersistenceAdapter
@RequiredArgsConstructor
public class OtpPersistenceAdapter implements SaveOtpPort, LoadOtpPort, VerifyOtpPort, DeleteOtpPort {

    private final RedisTemplate<String, OtpRedisEntity> redisTemplate;
    private final EncryptUtil encryptUtil;

    @Value("${otp.validity}")
    private Duration otpValidity;

    @Override
    public CurrentOtp saveOtp(final SaveOtp saveOtp) throws Exception {
        OtpType otpTypeConstant = getConstant(saveOtp.otpType());
        String token = StringUtil.randomNumeric(6);
        String otpKey = setOtpKey(otpTypeConstant, saveOtp.username());
        String encryptedToken = encryptUtil.encrypt(token);

        OtpRedisEntity otpRedisEntity = new OtpRedisEntity(saveOtp.username(), encryptedToken);

        ValueOperations<String, OtpRedisEntity> ops = redisTemplate.opsForValue();
        ops.set(otpKey, otpRedisEntity);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + otpValidity.toMillis());
        redisTemplate.expireAt(otpKey, expireDate);

        return new CurrentOtp(saveOtp.username(), otpTypeConstant.getLabel(), encryptedToken);
    }

    @Override
    public CurrentDecryptedOtp loadOtp(final LoadOtp loadOtp) throws Exception {
        OtpType otpTypeConstant = getConstant(loadOtp.otpType());
        ValueOperations<String, OtpRedisEntity> ops = redisTemplate.opsForValue();
        OtpRedisEntity otpRedisEntity = ops.get(setOtpKey(otpTypeConstant, loadOtp.username()));
        String decrypted = encryptUtil.decrypt(otpRedisEntity.getToken());
        return new CurrentDecryptedOtp(loadOtp.username(), otpTypeConstant.getLabel(), decrypted);
    }

    @Override
    public void verifyOtp(final VerifyOtp verifyOtp) throws Exception {
        OtpType otpTypeConstant = getConstant(verifyOtp.otpType());
        String otpKey = setOtpKey(otpTypeConstant, verifyOtp.username());
        ValueOperations<String, OtpRedisEntity> ops = redisTemplate.opsForValue();
        OtpRedisEntity otpRedisEntity = ops.get(otpKey);

        if (otpRedisEntity == null) throw new CommonApplicationException(EXCEPTION_OTP_NOT_FOUND);

        if (checkRedisExpiration(ops, otpKey))
            throw new CommonApplicationException(EXCEPTION_OTP_EXPIRED);

        if (!checkMatchValue(verifyOtp.otp(), otpRedisEntity))
            throw new CommonApplicationException(EXCEPTION_OTP_NOT_MATCH);

        expire(otpKey);
    }

    @Override
    public void expire(final String otpType, final String username) {
        OtpType constant = getConstant(otpType);
        String otpKey = setOtpKey(constant, username);
        expire(otpKey);
    }

    private void expire(final String otpKey) {
        redisTemplate.expire(otpKey, Duration.ZERO);
    }

    private Boolean checkRedisExpiration(final ValueOperations<String, OtpRedisEntity> ops, final String key) {
        return ops.get(key) == null || redisTemplate.getExpire(key) == null || redisTemplate.getExpire(key) < 0;
    }

    private Boolean checkMatchValue(@NotNull String value, @NotNull OtpRedisEntity otpRedisEntity) throws Exception {
        return Objects.equals(value, encryptUtil.decrypt(otpRedisEntity.getToken()));
    }

    private OtpType getConstant(final String otpType) {
        return fromLabel(otpType);
    }

    private String setOtpKey(final OtpType otpType, final String username) {
        return format("otp::%s::%s", otpType, username);
    }
}
