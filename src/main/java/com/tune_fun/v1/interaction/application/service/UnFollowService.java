package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.command.InteractionCommands;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnFollowUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class UnFollowService implements UnFollowUserUseCase {

    @Transactional
    @Override
    public void unfollow(final InteractionCommands.UnFollow command, final User user) {
        
    }

}
