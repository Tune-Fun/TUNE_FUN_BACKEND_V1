package com.tune_fun.v1.vote.adapter.input.message;

import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperRegisterFcmUseCase;
import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperUploadEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteMessageConsumer {

    private final SendVotePaperRegisterFcmUseCase sendVotePaperRegisterFcmUseCase;

    @SqsListener("send-vote-paper-upload-notification-dev")
    public void consumeVotePaperUploadEvent(final ProduceVotePaperUploadEvent produceVotePaperUploadEvent) {
        log.info("send-vote-paper-upload-notification-dev: {}", produceVotePaperUploadEvent.id());
        sendVotePaperRegisterFcmUseCase.send(produceVotePaperUploadEvent);
    }

}
