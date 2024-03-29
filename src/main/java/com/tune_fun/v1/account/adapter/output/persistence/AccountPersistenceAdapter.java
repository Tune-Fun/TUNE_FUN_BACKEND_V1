package com.tune_fun.v1.account.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AccountRepository;
import com.tune_fun.v1.account.application.port.output.*;
import com.tune_fun.v1.account.application.port.output.oauth2.SaveOAuth2AccountPort;
import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.behavior.SaveOAuth2Account;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.account.domain.state.RegisteredAccount;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;


@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements
        LoadAccountPort, SaveAccountPort, SaveOAuth2AccountPort,
        RecordLastLoginAtPort, RecordEmailVerifiedAtPort,
        UpdatePasswordPort, UpdateNicknamePort {

    private final AccountRepository accountRepository;
    private final OAuth2AccountRepository oauth2AccountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Optional<User> loadCustomUserByUsername(final String username) {
        return loadAccountByUsername(username)
                .map(account -> new User(account.getUsername(), account.getPassword(),
                        account.isEnabled(), account.isAccountNonExpired(), account.isCredentialsNonExpired(),
                        account.isAccountNonLocked(), account.getAuthorities()));
    }

    @Override
    public Optional<CurrentAccount> currentAccountInfo(final String username) {
        return loadAccountByUsername(username).map(accountMapper::accountInfo);
    }

    @Override
    public Optional<RegisteredAccount> registeredAccountInfoByUsername(final String username) {
        return loadAccountByUsername(username).map(accountMapper::registeredAccountInfo);
    }

    @Override
    public Optional<RegisteredAccount> registeredAccountInfoByEmail(final String email) {
        return findByEmail(email).map(accountMapper::registeredAccountInfo);
    }

    @Override
    public Optional<RegisteredAccount> registeredAccountInfoByNickname(final String nickname) {
        return findByNickname(nickname).map(accountMapper::registeredAccountInfo);
    }

    @Override
    public void recordLastLoginAt(final String username) {
        loadAccountByUsername(username)
                .ifPresent(account -> {
                    AccountJpaEntity updatedAccount = account.toBuilder()
                            .lastLoginAt(LocalDateTime.now()).build();
                    accountRepository.save(updatedAccount);
                });
    }

    @Override
    public void recordEmailVerifiedAt(final String username) {
        loadAccountByUsername(username)
                .ifPresent(account -> {
                    AccountJpaEntity updatedAccount = account.toBuilder()
                            .emailVerifiedAt(LocalDateTime.now()).build();
                    accountRepository.save(updatedAccount);
                });
    }

    public Optional<AccountJpaEntity> loadAccountByUsername(final String username) {
        return accountRepository.findActive(username, null, null);
    }

    public Optional<AccountJpaEntity> findByEmail(final String email) {
        return accountRepository.findActive(null, email, null);
    }

    public Optional<AccountJpaEntity> findByNickname(final String nickname) {
        return accountRepository.findActive(null, null, nickname);
    }

    @Override
    public CurrentAccount saveAccount(final SaveAccount behavior) {
        AccountJpaEntity saved = accountRepository.save(accountMapper.fromSaveAccountBehavior(behavior));
        return accountMapper.accountInfo(saved);
    }

    @Override
    public void saveOAuth2Account(final SaveOAuth2Account behavior) {
        loadAccountByUsername(behavior.username())
                .ifPresent(account -> {
                    OAuth2AccountJpaEntity updatedAccount = OAuth2AccountJpaEntity.builder()
                            .uuid(StringUtil.uuid())
                            .email(behavior.email())
                            .nickname(behavior.nickname())
                            .oauth2Provider(behavior.oauth2Provider())
                            .account(account)
                            .build();

                    oauth2AccountRepository.save(updatedAccount);
                });
    }

    @Override
    public void updatePassword(final String username, final String encodedPassword) {
        loadAccountByUsername(username)
                .ifPresent(account -> {
                    AccountJpaEntity updatedAccount = account.toBuilder()
                            .password(encodedPassword).build();
                    accountRepository.save(updatedAccount);
                });
    }

    @Override
    public void updateNickname(final String username, final String nickname) {
        loadAccountByUsername(username)
                .ifPresent(account -> {
                    AccountJpaEntity updatedAccount = account.toBuilder()
                            .nickname(nickname).build();
                    accountRepository.save(updatedAccount);
                });
    }
}
