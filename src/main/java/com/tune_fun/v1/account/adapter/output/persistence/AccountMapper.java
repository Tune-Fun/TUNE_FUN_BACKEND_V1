package com.tune_fun.v1.account.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AccountJpaEntity;
import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.account.domain.value.RegisteredAccount;
import com.tune_fun.v1.account.domain.value.Role;
import com.tune_fun.v1.account.domain.value.oauth2.RegisteredOAuth2Account;
import com.tune_fun.v1.common.config.BaseMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;

import static java.util.Collections.singleton;

@Mapper(
        config = BaseMapperConfig.class,
        imports = {Collections.class, Role.class}
)
public abstract class AccountMapper {

    public abstract CurrentAccount accountInfo(final AccountJpaEntity accountJpaEntity);

    @Mapping(target = "roles", source = "role", qualifiedByName = "roles")
    @Mapping(target = "notificationConfig.voteProgressNotification", source = "voteProgressNotification")
    @Mapping(target = "notificationConfig.voteEndNotification", source = "voteEndNotification")
    @Mapping(target = "notificationConfig.voteDeliveryNotification", source = "voteDeliveryNotification")
    public abstract AccountJpaEntity fromSaveAccountBehavior(final SaveAccount behavior);

    public abstract RegisteredAccount registeredAccountInfo(final AccountJpaEntity accountJpaEntity);

    @Named("roles")
    public Set<Role> roles(Role role) {
        return singleton(role);
    }

    public abstract RegisteredOAuth2Account registeredOAuth2AccountInfo(final OAuth2AccountJpaEntity oAuth2AccountJpaEntity);
}
