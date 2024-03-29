package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.lock.DistributionLock;
import com.tune_fun.v1.vote.application.port.input.command.VoteCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVoteUseCase;
import com.tune_fun.v1.vote.application.port.output.SaveVotePort;
import com.tune_fun.v1.vote.application.port.output.SendVoteUploadFcmPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVoteService implements RegisterVoteUseCase {

    private final SaveVotePort saveVotePort;
    private final SendVoteUploadFcmPort sendVoteUploadFcmPort;

    @Override
    @DistributionLock(key = "registerVote")
    public void register(final VoteCommands.Register command) {

        // TODO

    }


}
