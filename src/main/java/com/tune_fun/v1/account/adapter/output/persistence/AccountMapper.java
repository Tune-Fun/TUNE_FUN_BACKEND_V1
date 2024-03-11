package com.tune_fun.v1.account.adapter.output.persistence;

import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.account.domain.state.RegisteredAccount;
import com.tune_fun.v1.common.config.BaseMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(
        config = BaseMapperConfig.class,
        imports = {Collections.class, Role.class}
)
public abstract class AccountMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleValues")
    public abstract CurrentAccount accountInfo(final AccountJpaEntity accountJpaEntity);

    @Mapping(target = "roles", expression = "java(Collections.singletonList(Role.CLIENT_0))")
    @Mapping(target = "notificationConfig.voteProgressNotification", source = "voteProgressNotification")
    @Mapping(target = "notificationConfig.voteEndNotification", source = "voteEndNotification")
    @Mapping(target = "notificationConfig.voteDeliveryNotification", source = "voteDeliveryNotification")
    public abstract AccountJpaEntity fromSaveAccountBehavior(final SaveAccount behavior);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleValues")
    public abstract RegisteredAccount registeredAccountInfo(AccountJpaEntity accountJpaEntity);

    @Named("roleValues")
    public List<String> roleValues(List<Role> roles) {
        return Role.roleValues(roles);
    }

}
