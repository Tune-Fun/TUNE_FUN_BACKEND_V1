package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePaperPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_ONE_VOTE_PAPER_PER_USER;

@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVotePaperService implements RegisterVotePaperUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final SaveVotePaperPort saveVotePaperPort;

    @Override
    public void register(final VotePaperCommands.Register command, final User user) {
        validateRegistrableVotePaperCount(user);

    }

    public void validateRegistrableVotePaperCount(final User user) {
        if (loadVotePaperPort.loadRegisteredVotePaper(user.getUsername()).isPresent())
            throw new CommonApplicationException(VOTE_POLICY_ONE_VOTE_PAPER_PER_USER);
    }
}
