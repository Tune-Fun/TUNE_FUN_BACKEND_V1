package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVoteChoiceUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVoteChoicePort;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.SaveVoteChoicePort;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.tune_fun.v1.common.response.MessageCode.*;
import static com.tune_fun.v1.vote.domain.value.VotePaperOption.DENY_ADD_CHOICES;
import static java.util.Collections.singleton;

@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVoteChoiceService implements RegisterVoteChoiceUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final LoadVoteChoicePort loadVoteChoicePort;

    private final SaveVoteChoicePort saveVoteChoicePort;

    private final VoteBehaviorMapper voteBehaviorMapper;

    @Transactional
    @Override
    public void registerVoteChoice(final Long votePaperId, final VotePaperCommands.Offer offer, final User user) {
        RegisteredVotePaper registeredVotePaper = loadVotePaperPort.loadRegisteredVotePaper(votePaperId)
                .orElseThrow(() -> new CommonApplicationException(VOTE_PAPER_NOT_FOUND));

        validateVotePaperOption(registeredVotePaper);
        validateRegisteredVotePaper(votePaperId, user);

        Set<SaveVoteChoice> behavior = singleton(voteBehaviorMapper.saveVoteChoice(offer));
        saveVoteChoicePort.saveVoteChoice(votePaperId, behavior);
    }

    private static void validateVotePaperOption(RegisteredVotePaper registeredVotePaper) {
        if (DENY_ADD_CHOICES.equals(registeredVotePaper.option()))
            throw new CommonApplicationException(VOTE_POLICY_ONLY_REGISTER_CHOICE_ON_ALLOW_ADD_CHOICES_OPTION);
    }

    public void validateRegisteredVotePaper(Long votePaperId, User user) {
        if (loadVoteChoicePort.loadVoteChoiceByUsername(votePaperId, user.getUsername()).isPresent())
            throw new CommonApplicationException(VOTE_POLICY_ONE_VOTE_CHOICE_PER_USER_ON_VOTE_PAPER);
    }

}
