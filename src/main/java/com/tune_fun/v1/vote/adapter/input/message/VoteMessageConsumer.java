package com.tune_fun.v1.vote.adapter.input.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperRegisterFcmUseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperUpdateDeliveryDateFcmUseCase;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteMessageConsumer {

    private final SendVotePaperRegisterFcmUseCase sendVotePaperRegisterFcmUseCase;
    private final SendVotePaperUpdateDeliveryDateFcmUseCase sendVotePaperUpdateDeliveryDateFcmUseCase;

    @SqsListener(value = "${event.sqs.send-vote-paper-upload-notification.queue-name}")
    public void consumeVotePaperUploadEvent(final VotePaperRegisterEvent votePaperRegisterEvent) throws JsonProcessingException, FirebaseMessagingException {
        log.info("send-vote-paper-upload-notification-dev: {}", votePaperRegisterEvent.id());
        sendVotePaperRegisterFcmUseCase.send(votePaperRegisterEvent);
    }

    @SqsListener(value = "${event.sqs.send-vote-paper-update-delivery-date-notification.queue-name}")
    public void consumeVotePaperUpdateDeliveryDateEvent(final VotePaperUpdateDeliveryDateEvent votePaperUpdateDeliveryDateEvent) throws JsonProcessingException, FirebaseMessagingException {
        log.info("send-vote-paper-update-delivery-date-notification-dev: {}", votePaperUpdateDeliveryDateEvent.id());
        sendVotePaperUpdateDeliveryDateFcmUseCase.send(votePaperUpdateDeliveryDateEvent);
    }

}
