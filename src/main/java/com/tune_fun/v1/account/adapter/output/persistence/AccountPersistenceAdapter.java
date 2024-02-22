package com.tune_fun.v1.account.adapter.output.persistence;

import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.RecordEmailVerifiedAtPort;
import com.tune_fun.v1.account.application.port.output.RecordLastLoginAtPort;
import com.tune_fun.v1.account.application.port.output.SaveAccountPort;
import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.account.domain.state.RegisteredAccount;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements
        LoadAccountPort, SaveAccountPort,
        RecordLastLoginAtPort, RecordEmailVerifiedAtPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadCustomUserByUsername(String username) {
        return loadAccountByUsername(username)
                .map(account -> new User(account.getUsername(), account.getPassword(), account.getAuthorities()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrentAccount> currentAccountInfo(String username) {
        return loadAccountByUsername(username)
                .map(accountMapper::accountInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegisteredAccount> registeredAccountInfo(String username) {
        return loadAccountByUsername(username)
                .map(accountMapper::registeredAccountInfo);
    }

    @Override
    @Transactional
    public void recordLastLoginAt(final String username) {
        loadAccountByUsername(username)
                .ifPresent(account -> {
                    AccountJpaEntity updatedAccount = account.toBuilder()
                            .lastLoginAt(LocalDateTime.now()).build();
                    accountRepository.save(updatedAccount);
                });
    }

    @Override
    @Transactional
    public void recordEmailVerifiedAt(final String username) {
        loadAccountByUsername(username)
                .ifPresent(account -> {
                    AccountJpaEntity updatedAccount = account.toBuilder()
                            .emailVerifiedAt(LocalDateTime.now()).build();
                    accountRepository.save(updatedAccount);
                });
    }

    @Transactional(readOnly = true)
    public Optional<AccountJpaEntity> loadAccountByUsername(String username) {
        return accountRepository.findByUsernameActive(username);
    }

    @Override
    @Transactional
    public CurrentAccount saveAccount(SaveAccount saveAccount) {
        AccountJpaEntity saved = accountRepository.save(accountMapper.fromSaveAccountValue(saveAccount));
        return accountMapper.accountInfo(saved);
    }
}
