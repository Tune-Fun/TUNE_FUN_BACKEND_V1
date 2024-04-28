package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUploadEventPort;
import com.tune_fun.v1.vote.application.port.output.SaveVoteChoicePort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePaperPort;
import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperUploadEvent;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_ONE_VOTE_PAPER_PER_USER;

@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVotePaperService implements RegisterVotePaperUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final SaveVotePaperPort saveVotePaperPort;

    private final SaveVoteChoicePort saveVoteChoicePort;

    private final ProduceVotePaperUploadEventPort produceVotePaperUploadEventPort;

    private final VoteBehaviorMapper voteBehaviorMapper;


    @Transactional
    @Override
    public void register(final VotePaperCommands.Register command, final User user) {
        validateRegistrableVotePaperCount(user);
        RegisteredVotePaper registeredVotePaper = saveVotePaper(command);
        saveVoteChoiceByRegisteredVotePaper(command, registeredVotePaper);

        // TODO : Scheduling 정책 수립
//        ProduceVotePaperUploadEvent produceVotePaperUploadEventBehavior = getProduceVotePaperUploadEventBehavior(registeredVotePaper);
//        produceVotePaperUploadEventPort.produceVotePaperUploadEvent(produceVotePaperUploadEventBehavior);
    }

    public void validateRegistrableVotePaperCount(final User user) {
        if (loadVotePaperPort.loadRegisteredVotePaper(user.getUsername()).isPresent())
            throw new CommonApplicationException(VOTE_POLICY_ONE_VOTE_PAPER_PER_USER);
    }

    @Transactional
    public RegisteredVotePaper saveVotePaper(VotePaperCommands.Register command) {
        SaveVotePaper saveVotePaperBehavior = voteBehaviorMapper.saveVotePaper(command);
        return saveVotePaperPort.saveVotePaper(saveVotePaperBehavior);
    }

    @Transactional
    public void saveVoteChoiceByRegisteredVotePaper(VotePaperCommands.Register command, RegisteredVotePaper registeredVotePaper) {
        Set<SaveVoteChoice> saveVoteChoicesBehavior = voteBehaviorMapper.saveVoteChoice(command.offers());
        saveVoteChoicePort.saveVoteChoice(registeredVotePaper.id(), saveVoteChoicesBehavior);
    }

    private static @NotNull ProduceVotePaperUploadEvent getProduceVotePaperUploadEventBehavior(RegisteredVotePaper registeredVotePaper) {
        return new ProduceVotePaperUploadEvent(registeredVotePaper.id(), registeredVotePaper.title(), registeredVotePaper.content(),
                registeredVotePaper.voteStartAt(), registeredVotePaper.voteEndAt());
    }
}
