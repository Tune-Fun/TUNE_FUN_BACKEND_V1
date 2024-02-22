package com.tune_fun.v1.dummy;


import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.RegisterUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.SendForgotPasswordOtpUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.GenerateAccessTokenUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.GenerateRefreshTokenUseCase;
import com.tune_fun.v1.base.AbstractIntegrationTest;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpPersistenceAdapter;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;
import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.application.port.input.usecase.VerifyOtpUseCase;
import com.tune_fun.v1.otp.domain.behavior.LoadOtp;
import com.tune_fun.v1.otp.domain.state.CurrentDecryptedOtp;
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

@Getter
@Service
public class DummyService extends AbstractIntegrationTest {

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
    private OtpPersistenceAdapter otpPersistenceAdapter;

    private AccountJpaEntity defaultAccount = null;

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

        registerUseCase.register(command);

        defaultAccount = accountPersistenceAdapter.loadAccountByUsername(defaultUsername)
                .orElseThrow(() -> new RuntimeException("initUser 실패"));
    }

    @Transactional
    public void login(final AccountJpaEntity account) {
        defaultAccessToken = generateAccessTokenUseCase.generateAccessToken(account);
        defaultRefreshToken = generateRefreshTokenUseCase.generateRefreshToken(account);

        User user = new User(account.getUsername(), account.getPassword(), account.getAuthorities());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Transactional
    public void forgotPasswordOtp() throws Exception {
        AccountCommands.SendForgotPasswordOtp command = new AccountCommands.SendForgotPasswordOtp(defaultUsername);
        assertDoesNotThrow(() -> sendForgotPasswordOtpUseCase.sendOtp(command));
        forgotPasswordOtp = otpPersistenceAdapter.loadOtp(new LoadOtp(defaultUsername, FORGOT_PASSWORD));
    }

    @Transactional
    public void verifyOtp(OtpType otpType, String token ) throws Exception {
        verifyOtpUseCase.verify(new OtpQueries.Verify(defaultUsername, otpType, token));
    }

    public void expireOtp(OtpType otpType) {
        otpPersistenceAdapter.expire(otpType, defaultUsername);
    }
}
