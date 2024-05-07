package com.tune_fun.v1.vote.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperRegisterEventPort;
import com.tune_fun.v1.vote.application.port.output.SaveVoteChoicePort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePaperPort;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_OFFERS_COUNT_SHOULD_BE_MORE_THAN_TWO;
import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_ONE_VOTE_PAPER_PER_USER;
import static com.tune_fun.v1.vote.domain.value.VotePaperOption.DENY_ADD_CHOICES;

@Slf4j
@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVotePaperService implements RegisterVotePaperUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final SaveVotePaperPort saveVotePaperPort;

    private final SaveVoteChoicePort saveVoteChoicePort;

    private final ProduceVotePaperRegisterEventPort produceVotePaperRegisterEventPort;

    private final VoteBehaviorMapper voteBehaviorMapper;


    @Transactional
    @Override
    public void register(final VotePaperCommands.Register command, final User user) throws JsonProcessingException {
        validateOffersCount(command);

        validateRegistrableVotePaperCount(user);
        RegisteredVotePaper registeredVotePaper = saveVotePaper(command, user);
        saveVoteChoiceByRegisteredVotePaper(command, registeredVotePaper);

        VotePaperRegisterEvent votePaperRegisterEventBehavior = getProduceVotePaperUploadEventBehavior(registeredVotePaper);
        produceVotePaperRegisterEventPort.produceVotePaperUploadEvent(votePaperRegisterEventBehavior);
    }

    private static void validateOffersCount(final VotePaperCommands.Register command) {
        if (DENY_ADD_CHOICES.equals(command.option()) && command.offers().size() < 2)
            throw new CommonApplicationException(VOTE_POLICY_OFFERS_COUNT_SHOULD_BE_MORE_THAN_TWO);
    }

    public void validateRegistrableVotePaperCount(final User user) {
        if (loadVotePaperPort.loadRegisteredVotePaper(user.getUsername()).isPresent())
            throw new CommonApplicationException(VOTE_POLICY_ONE_VOTE_PAPER_PER_USER);
    }

    @Transactional
    public RegisteredVotePaper saveVotePaper(final VotePaperCommands.Register command, final User user) {
        SaveVotePaper saveVotePaperBehavior = voteBehaviorMapper.saveVotePaper(command, user);
        return saveVotePaperPort.saveVotePaper(saveVotePaperBehavior);
    }

    @Transactional
    public void saveVoteChoiceByRegisteredVotePaper(VotePaperCommands.Register command, RegisteredVotePaper registeredVotePaper) {
        Set<SaveVoteChoice> saveVoteChoicesBehavior = voteBehaviorMapper.saveVoteChoices(command.offers());
        saveVoteChoicePort.saveVoteChoice(registeredVotePaper.id(), saveVoteChoicesBehavior);
    }

    private static @NotNull VotePaperRegisterEvent getProduceVotePaperUploadEventBehavior(RegisteredVotePaper registeredVotePaper) {
        return new VotePaperRegisterEvent(registeredVotePaper.uuid(), registeredVotePaper.author(), registeredVotePaper.title(),
                registeredVotePaper.content(), registeredVotePaper.voteEndAt());
    }
}
