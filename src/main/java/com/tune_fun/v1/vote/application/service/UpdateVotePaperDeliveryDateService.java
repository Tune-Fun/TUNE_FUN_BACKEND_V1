package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.UpdateVotePaperDeliveryDateUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUpdateDeliveryDateEventPort;
import com.tune_fun.v1.vote.application.port.output.UpdateDeliveryAtPort;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_ONLY_AUTHOR_CAN_UPDATE_DELIVERY_DATE;

@Service
@UseCase
@RequiredArgsConstructor
public class UpdateVotePaperDeliveryDateService implements UpdateVotePaperDeliveryDateUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final UpdateDeliveryAtPort updateDeliveryAtPort;

    private final ProduceVotePaperUpdateDeliveryDateEventPort produceVotePaperUploadEventPort;

    @Transactional
    @Override
    public void updateDeliveryDate(final Long votePaperId, final VotePaperCommands.UpdateDeliveryDate command, final User user) {
        loadVotePaperPort.loadRegisteredVotePaper(user.getUsername())
                .orElseThrow(() -> new CommonApplicationException(VOTE_POLICY_ONLY_AUTHOR_CAN_UPDATE_DELIVERY_DATE));

        RegisteredVotePaper registeredVotePaper = updateDeliveryAtPort.updateDeliveryAt(votePaperId, command.deliveryAt());

        VotePaperUpdateDeliveryDateEvent event = new VotePaperUpdateDeliveryDateEvent(registeredVotePaper.uuid(),
                registeredVotePaper.author(), registeredVotePaper.title(), registeredVotePaper.content());
        produceVotePaperUploadEventPort.produceVotePaperUpdateDeliveryDateEvent(event);
    }

}
