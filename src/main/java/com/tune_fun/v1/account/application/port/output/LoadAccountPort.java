package com.tune_fun.v1.account.application.port.output;

import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.account.domain.value.RegisteredAccount;
import com.tune_fun.v1.account.domain.value.oauth2.RegisteredOAuth2Account;
import com.tune_fun.v1.interaction.domain.ScrollableArtist;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface LoadAccountPort {

    Optional<User> loadCustomUserByUsername(final String username);

    Optional<CurrentAccount> currentAccountInfo(final String username);

    Optional<RegisteredAccount> registeredAccountInfoByUsername(final String username);

    Optional<RegisteredAccount> registeredAccountInfoByEmail(final String email);

    Optional<RegisteredAccount> registeredAccountInfoByNickname(final String nickname);

    Optional<RegisteredOAuth2Account> registeredOAuth2AccountInfoByEmail(final String email);

    Slice<ScrollableArtist> scrollArtist(final Long lastId, final String nickname);

}
