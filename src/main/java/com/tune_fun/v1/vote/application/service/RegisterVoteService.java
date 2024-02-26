package com.tune_fun.v1.vote.application.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.lock.DistributionLock;
import com.tune_fun.v1.vote.application.port.input.command.VoteCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVoteUseCase;
import com.tune_fun.v1.vote.application.port.output.ProduceVoteUploadEventPort;
import com.tune_fun.v1.vote.application.port.output.SaveVoteImagePort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePort;
import com.tune_fun.v1.vote.application.port.output.SendVoteUploadFcmPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@XRayEnabled
@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVoteService implements RegisterVoteUseCase {

    private final SaveVotePort saveVotePort;
    private final SaveVoteImagePort saveVoteImagePort;
    private final SendVoteUploadFcmPort sendVoteUploadFcmPort;

    @Override
    @DistributionLock(key = "registerVote")
    public void register(final VoteCommands.Register command) {

        // TODO

    }


}
