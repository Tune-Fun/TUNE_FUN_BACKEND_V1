package com.tune_fun.v1.vote.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.util.ObjectUtil;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUploadEventPort;
import com.tune_fun.v1.vote.application.port.output.SaveVoteChoicePort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePaperPort;
import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import io.awspring.cloud.sqs.operations.SendResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_ONE_VOTE_PAPER_PER_USER;

@Slf4j
@Service
@UseCase
@RequiredArgsConstructor
public class RegisterVotePaperService implements RegisterVotePaperUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final SaveVotePaperPort saveVotePaperPort;

    private final SaveVoteChoicePort saveVoteChoicePort;

    private final ProduceVotePaperUploadEventPort produceVotePaperUploadEventPort;

    private final VoteBehaviorMapper voteBehaviorMapper;

    private final ObjectUtil objectUtil;


    @Transactional
    @Override
    public void register(final VotePaperCommands.Register command, final User user) throws JsonProcessingException {
        validateRegistrableVotePaperCount(user);
        RegisteredVotePaper registeredVotePaper = saveVotePaper(command);
        saveVoteChoiceByRegisteredVotePaper(command, registeredVotePaper);

        ProduceVotePaperRegisterEvent produceVotePaperRegisterEventBehavior = getProduceVotePaperUploadEventBehavior(registeredVotePaper);
        SendResult<?> sendResult = produceVotePaperUploadEventPort.produceVotePaperUploadEvent(produceVotePaperRegisterEventBehavior);

        log.info("sendResult: \n{}", objectUtil.objectToPrettyJson(sendResult.message().getPayload()));
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
        Set<SaveVoteChoice> saveVoteChoicesBehavior = voteBehaviorMapper.saveVoteChoices(command.offers());
        saveVoteChoicePort.saveVoteChoice(registeredVotePaper.id(), saveVoteChoicesBehavior);
    }

    private static @NotNull ProduceVotePaperRegisterEvent getProduceVotePaperUploadEventBehavior(RegisteredVotePaper registeredVotePaper) {
        return new ProduceVotePaperRegisterEvent(registeredVotePaper.uuid(), registeredVotePaper.author(), registeredVotePaper.title(), registeredVotePaper.content());
    }
}
