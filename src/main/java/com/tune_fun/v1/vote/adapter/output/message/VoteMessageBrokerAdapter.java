package com.tune_fun.v1.vote.adapter.output.message;

import com.tune_fun.v1.external.aws.sqs.SqsProvider;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUploadEventPort;
import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperRegisterEvent;
import io.awspring.cloud.sqs.operations.SendResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteMessageBrokerAdapter implements ProduceVotePaperUploadEventPort {

    private final SqsProvider sqsProvider;
    private static final String VOTE_PAPER_UPLOAD_QUEUE = "send-vote-paper-upload-notification-dev";


    /**
     * @param produceVotePaperRegisterEvent {@link ProduceVotePaperRegisterEvent}
     * @return {@link SendResult}
     * @see com.tune_fun.v1.vote.adapter.input.message.VoteMessageConsumer#consumeVotePaperUploadEvent(ProduceVotePaperRegisterEvent)
     */
    @Override
    public SendResult<?> produceVotePaperUploadEvent(final ProduceVotePaperRegisterEvent produceVotePaperRegisterEvent) {
        return sqsProvider.sendMessageRangedQueue(VOTE_PAPER_UPLOAD_QUEUE, produceVotePaperRegisterEvent);
    }
}
