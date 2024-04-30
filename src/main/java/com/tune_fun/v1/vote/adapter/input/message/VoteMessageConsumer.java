package com.tune_fun.v1.vote.adapter.input.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperRegisterFcmUseCase;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteMessageConsumer {

    private final SendVotePaperRegisterFcmUseCase sendVotePaperRegisterFcmUseCase;

    @SqsListener(value = "send-vote-paper-upload-notification-dev")
    public void consumeVotePaperUploadEvent(final VotePaperRegisterEvent votePaperRegisterEvent) throws JsonProcessingException, FirebaseMessagingException {
        log.info("send-vote-paper-upload-notification-dev: {}", votePaperRegisterEvent.id());
        sendVotePaperRegisterFcmUseCase.send(votePaperRegisterEvent);
    }

}
