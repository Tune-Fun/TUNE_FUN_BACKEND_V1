package com.tune_fun.v1.account.application.port.output;

import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.account.domain.state.RegisteredAccount;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface LoadAccountPort {

    Optional<User> loadCustomUserByUsername(String username);

    Optional<CurrentAccount> currentAccountInfo(String username);

    Optional<RegisteredAccount> registeredAccountInfo(String username);

}
