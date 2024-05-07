package com.tune_fun.v1.vote.adapter.input.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperRegisterNotificationUseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperUpdateDeliveryDateNotificationUseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperUpdateVideoUrlNotificationUseCase;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateVideoUrlEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode.ALWAYS;

/**
 * @see <a href="https://www.baeldung.com/java-spring-cloud-aws-v3-message-acknowledgement">Message Acknowledgement in Spring Cloud AWS SQS v3</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VoteMessageConsumer {

    private final SendVotePaperRegisterNotificationUseCase sendVotePaperRegisterNotificationUseCase;
    private final SendVotePaperUpdateDeliveryDateNotificationUseCase sendVotePaperUpdateDeliveryDateNotificationUseCase;
    private final SendVotePaperUpdateVideoUrlNotificationUseCase sendVotePaperUpdateVideoUrlNotificationUseCase;

    @SqsListener(value = "${event.sqs.send-vote-paper-upload-notification.queue-name}", acknowledgementMode = ALWAYS)
    public void consumeVotePaperUploadEvent(final VotePaperRegisterEvent event) throws JsonProcessingException, FirebaseMessagingException {
        log.info("CONSUME 'send-vote-paper-upload-notification': {}", event.id());
        sendVotePaperRegisterNotificationUseCase.send(event);
    }

    @SqsListener(value = "${event.sqs.send-vote-paper-update-delivery-date-notification.queue-name}", acknowledgementMode = ALWAYS)
    public void consumeVotePaperUpdateDeliveryDateEvent(final VotePaperUpdateDeliveryDateEvent event) throws FirebaseMessagingException {
        log.info("CONSUME 'send-vote-paper-update-delivery-date-notification': {}", event.id());
        sendVotePaperUpdateDeliveryDateNotificationUseCase.send(event);
    }

    @SqsListener(value = "${event.sqs.send-vote-paper-update-video-url-notification.queue-name}", acknowledgementMode = ALWAYS)
    public void consumeVotePaperUpdateVideoUrlEvent(final VotePaperUpdateVideoUrlEvent event) throws FirebaseMessagingException {
        log.info("CONSUME 'send-vote-paper-update-video-url-notification': {}", event.id());
        sendVotePaperUpdateVideoUrlNotificationUseCase.send(event);
    }

}
