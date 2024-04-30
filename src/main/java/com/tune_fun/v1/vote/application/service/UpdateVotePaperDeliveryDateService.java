package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.UpdateVotePaperDeliveryDateUseCase;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUpdateDeliveryDateEventPort;
import com.tune_fun.v1.vote.application.port.output.UpdateDeliveryAtPort;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class UpdateVotePaperDeliveryDateService implements UpdateVotePaperDeliveryDateUseCase {

    private final UpdateDeliveryAtPort updateDeliveryAtPort;

    private final ProduceVotePaperUpdateDeliveryDateEventPort produceVotePaperUploadEventPort;


    @Transactional
    @Override
    public void updateDeliveryDate(final VotePaperCommands.UpdateDeliveryDate command) {
        RegisteredVotePaper registeredVotePaper = updateDeliveryAtPort.updateDeliveryAt(command.votePaperId(), command.deliveryAt());

        VotePaperUpdateDeliveryDateEvent event = new VotePaperUpdateDeliveryDateEvent(registeredVotePaper.uuid(),
                registeredVotePaper.author(), registeredVotePaper.title(), registeredVotePaper.content());
        produceVotePaperUploadEventPort.produceVotePaperUpdateDeliveryDateEvent(event);
    }

}
