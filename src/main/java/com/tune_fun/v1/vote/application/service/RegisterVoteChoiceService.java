package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVoteChoiceUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import com.tune_fun.v1.vote.domain.value.VotePaperOption;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import static com.tune_fun.v1.common.response.MessageCode.VOTE_PAPER_NOT_FOUND;
import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_ONLY_REGISTER_CHOICE_ON_ALLOW_ADD_CHOICES_OPTION;
import static com.tune_fun.v1.vote.domain.value.VotePaperOption.DENY_ADD_CHOICES;

@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVoteChoiceService implements RegisterVoteChoiceUseCase {

    private final LoadVotePaperPort loadVotePaperPort;

    @Override
    public void registerVoteChoice(final Long votePaperId, final VotePaperCommands.Offer offer, final User user) {
        RegisteredVotePaper registeredVotePaper = loadVotePaperPort.loadRegisteredVotePaper(votePaperId)
                .orElseThrow(() -> new CommonApplicationException(VOTE_PAPER_NOT_FOUND));

        if (DENY_ADD_CHOICES.equals(registeredVotePaper.option()))
            throw new CommonApplicationException(VOTE_POLICY_ONLY_REGISTER_CHOICE_ON_ALLOW_ADD_CHOICES_OPTION);



    }

}
