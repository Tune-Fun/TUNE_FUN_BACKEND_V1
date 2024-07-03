package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.command.InteractionCommands;
import com.tune_fun.v1.interaction.application.port.input.usecase.FollowUserUseCase;
import com.tune_fun.v1.interaction.application.port.output.LoadFollowPort;
import com.tune_fun.v1.interaction.application.port.output.SaveFollowPort;
import com.tune_fun.v1.interaction.domain.RegisteredFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@UseCase
@RequiredArgsConstructor
public class FollowService implements FollowUserUseCase {

    private final LoadAccountPort loadAccountPort;
    private final LoadFollowPort loadFollowPort;
    private final SaveFollowPort saveFollowPort;

    private static final Consumer<RegisteredFollow> THROW_ALREADY_FOLLOWED = follow -> {
        throw CommonApplicationException.ALREADY_FOLLOWED;
    };

    @Transactional
    @Override
    public void follow(final InteractionCommands.Follow command, final User user) {
        CurrentAccount currentAccount = loadAccountPort.currentAccountInfo(user.getUsername())
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);
        Long followerAccountId = currentAccount.id();

        loadFollowPort.loadFollow(command.targetAccountId(), followerAccountId)
                .ifPresentOrElse(THROW_ALREADY_FOLLOWED, () -> saveFollowPort.saveFollow(command.targetAccountId(), followerAccountId));
    }
}
