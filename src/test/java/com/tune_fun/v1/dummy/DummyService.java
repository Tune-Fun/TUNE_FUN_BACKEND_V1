package com.tune_fun.v1.dummy;


import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.RegisterUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.GenerateAccessTokenUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.GenerateRefreshTokenUseCase;
import com.tune_fun.v1.base.AbstractIntegrationTest;
import com.tune_fun.v1.common.util.StringUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

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
    private AccountPersistenceAdapter accountPersistenceAdapter;

    private AccountJpaEntity defaultAccount = null;

    private String defaultUsername = null;
    private String defaultPassword = null;
    private String defaultEmail = null;

    private String defaultAccessToken = null;
    private String defaultRefreshToken = null;

    @Transactional
    public void initAndLogin() throws NoSuchAlgorithmException {
        initUser();
        login(defaultAccount);
    }

    @Transactional
    public void initUser() throws NoSuchAlgorithmException {
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
}
