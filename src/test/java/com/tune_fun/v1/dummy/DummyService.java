package com.tune_fun.v1.dummy;


import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.adapter.output.persistence.device.DeviceJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.device.DevicePersistenceAdapter;
import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.RegisterUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.SendForgotPasswordOtpUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.GenerateAccessTokenUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.GenerateRefreshTokenUseCase;
import com.tune_fun.v1.account.domain.behavior.SaveDevice;
import com.tune_fun.v1.base.annotation.IntegrationTest;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpPersistenceAdapter;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;
import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.application.port.input.usecase.VerifyOtpUseCase;
import com.tune_fun.v1.otp.domain.behavior.LoadOtp;
import com.tune_fun.v1.otp.domain.value.CurrentDecryptedOtp;
import com.tune_fun.v1.otp.domain.value.VerifyResult;
import com.tune_fun.v1.vote.adapter.input.rest.RegisterType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

import static com.tune_fun.v1.otp.adapter.output.persistence.OtpType.FORGOT_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@IntegrationTest
@Getter
@Service
public class DummyService {

    @Autowired
    private RegisterUseCase registerUseCase;

    @Autowired
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Autowired
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Autowired
    private SendForgotPasswordOtpUseCase sendForgotPasswordOtpUseCase;

    @Autowired
    private VerifyOtpUseCase verifyOtpUseCase;

    @Autowired
    private AccountPersistenceAdapter accountPersistenceAdapter;

    @Autowired
    private DevicePersistenceAdapter devicePersistenceAdapter;

    @Autowired
    private OtpPersistenceAdapter otpPersistenceAdapter;

    private AccountJpaEntity defaultAccount = null;
    private DeviceJpaEntity defaultDevice = null;

    private String defaultUsername = null;
    private String defaultPassword = null;
    private String defaultEmail = null;

    private String defaultAccessToken = null;
    private String defaultRefreshToken = null;

    private CurrentDecryptedOtp forgotPasswordOtp = null;

    @Transactional
    public void initAndLogin() throws NoSuchAlgorithmException {
        initAccount();
        login(defaultAccount);
    }

    @Transactional
    public void initAccount() throws NoSuchAlgorithmException {
        defaultUsername = StringUtil.randomAlphanumeric(10, 15);
        defaultPassword = StringUtil.randomAlphaNumericSymbol(15, 20);
        defaultEmail = StringUtil.randomAlphabetic(7) + "@" + StringUtil.randomAlphabetic(5) + ".com";
        String nickname = StringUtil.randomAlphabetic(5);

        AccountCommands.Notification notification = new AccountCommands.Notification(true, true, true);
        AccountCommands.Register command = new AccountCommands.Register(defaultUsername, defaultPassword, defaultEmail, nickname, notification);

        registerUseCase.register(RegisterType.NORMAL, command);

        defaultAccount = accountPersistenceAdapter.loadAccountByUsername(defaultUsername)
                .orElseThrow(() -> new RuntimeException("initUser 실패"));
    }

    @Transactional
    public void login(final AccountJpaEntity account) throws NoSuchAlgorithmException {
        defaultAccessToken = generateAccessTokenUseCase.generateAccessToken(account);
        defaultRefreshToken = generateRefreshTokenUseCase.generateRefreshToken(account);

        SaveDevice saveDeviceBehavior = new SaveDevice(account.getUsername(), StringUtil.randomAlphaNumericSymbol(15), StringUtil.randomAlphaNumericSymbol(15));
        devicePersistenceAdapter.saveDevice(saveDeviceBehavior);
        devicePersistenceAdapter.findByFcmTokenOrDeviceToken(saveDeviceBehavior.username(), saveDeviceBehavior.fcmToken(), saveDeviceBehavior.deviceToken())
                .ifPresent(device -> defaultDevice = device);

        User user = new User(account.getUsername(), account.getPassword(), account.getAuthorities());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Transactional
    public void forgotPasswordOtp() throws Exception {
        AccountCommands.SendForgotPasswordOtp command = new AccountCommands.SendForgotPasswordOtp(defaultUsername);
        assertDoesNotThrow(() -> sendForgotPasswordOtpUseCase.sendOtp(command));
        forgotPasswordOtp = otpPersistenceAdapter.loadOtp(new LoadOtp(defaultUsername, FORGOT_PASSWORD.getLabel()));
    }

    @Transactional
    public void verifyOtp(OtpType otpType, String token) throws Exception {
        OtpQueries.Verify query = new OtpQueries.Verify(defaultUsername, otpType.getLabel(), token);
        VerifyResult verifyResult = verifyOtpUseCase.verify(query);

        defaultAccessToken = verifyResult.accessToken();
        defaultRefreshToken = verifyResult.refreshToken();
    }

    public void expireOtp(OtpType otpType) {
        otpPersistenceAdapter.expire(otpType.getLabel(), defaultUsername);
    }
}
