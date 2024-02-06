package com.habin.demo.account.application.port.output;

import com.habin.demo.account.domain.state.CurrentAccount;
import com.habin.demo.account.domain.state.RegisteredAccount;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface LoadAccountPort {

    Optional<User> loadCustomUserByUsername(String username);

    Optional<CurrentAccount> accountInfo(String username);

    Optional<RegisteredAccount> registeredAccountInfo(String username);

}
