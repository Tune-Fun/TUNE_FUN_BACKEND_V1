package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.lock.DistributionLock;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVoteUseCase;
import com.tune_fun.v1.vote.application.port.output.SaveVotePort;
import com.tune_fun.v1.vote.application.port.output.SendVoteFcmPort;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;


@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVoteService implements RegisterVoteUseCase {

    private final SaveVotePort saveVotePort;

    @Override
    public void register(final Long voteChoiceId, final User user) {
        saveVotePort.saveVote(voteChoiceId, user.getUsername());
    }


}
