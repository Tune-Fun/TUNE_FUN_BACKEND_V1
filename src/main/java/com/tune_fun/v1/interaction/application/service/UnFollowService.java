package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.command.InteractionCommands;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnFollowUserUseCase;
import com.tune_fun.v1.interaction.application.port.output.DeleteFollowPort;
import com.tune_fun.v1.interaction.application.port.output.LoadFollowPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class UnFollowService implements UnFollowUserUseCase {

    private static final Runnable THROW_NOT_FOLLOWED = CommonApplicationException.NOT_FOLLOWED::get;

    private final LoadAccountPort loadAccountPort;
    private final LoadFollowPort loadFollowPort;
    private final DeleteFollowPort deleteFollowPort;

    @Transactional
    @Override
    public void unfollow(final InteractionCommands.UnFollow command, final User user) {
        CurrentAccount currentAccount = loadAccountPort.currentAccountInfo(user.getUsername())
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);
        Long followerAccountId = currentAccount.id();

        loadFollowPort.loadFollow(command.targetAccountId(), followerAccountId)
                .ifPresentOrElse(follow -> deleteFollowPort.deleteFollow(follow.followeeId(), follow.followerId()), THROW_NOT_FOLLOWED);
    }

}
