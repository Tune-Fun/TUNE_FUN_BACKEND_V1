package com.tune_fun.v1.vote.adapter.input.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperRegisterNotificationUseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperUpdateDeliveryDateNotificationUseCase;
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

    private final SendVotePaperRegisterNotificationUseCase sendVotePaperRegisterNotificationUseCase;
    private final SendVotePaperUpdateDeliveryDateNotificationUseCase sendVotePaperUpdateDeliveryDateNotificationUseCase;

    @SqsListener(value = "${event.sqs.send-vote-paper-upload-notification.queue-name}")
    public void consumeVotePaperUploadEvent(final VotePaperRegisterEvent event) throws JsonProcessingException, FirebaseMessagingException {
        log.info("CONSUME 'send-vote-paper-upload-notification-dev': {}", event.id());
        sendVotePaperRegisterNotificationUseCase.send(event);
    }

    @SqsListener(value = "${event.sqs.send-vote-paper-update-delivery-date-notification.queue-name}")
    public void consumeVotePaperUpdateDeliveryDateEvent(final VotePaperUpdateDeliveryDateEvent event) throws FirebaseMessagingException {
        log.info("CONSUME 'send-vote-paper-update-delivery-date-notification-dev': {}", event.id());
        sendVotePaperUpdateDeliveryDateNotificationUseCase.send(event);
    }

}
