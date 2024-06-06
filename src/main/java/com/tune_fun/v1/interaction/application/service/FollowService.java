package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.command.InteractionCommands;
import com.tune_fun.v1.interaction.application.port.input.usecase.FollowUserUseCase;
import com.tune_fun.v1.interaction.application.port.output.LoadFollowPort;
import com.tune_fun.v1.interaction.application.port.output.SaveFollowPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class FollowService implements FollowUserUseCase {

    private final LoadFollowPort loadFollowPort;
    private final SaveFollowPort saveFollowPort;

    @Transactional
    @Override
    public void follow(final InteractionCommands.Follow command, final User user) {


    }
}
