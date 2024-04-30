package com.tune_fun.v1.vote.adapter.output.message;

import com.tune_fun.v1.common.property.EventProperty;
import com.tune_fun.v1.external.aws.sqs.SqsProvider;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUpdateDeliveryDateEventPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUploadEventPort;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import io.awspring.cloud.sqs.operations.SendResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteMessageBrokerAdapter implements
        ProduceVotePaperUploadEventPort, ProduceVotePaperUpdateDeliveryDateEventPort {

    private final SqsProvider sqsProvider;
    private final EventProperty eventProperty;

    private static final String VOTE_PAPER_UPLOAD_QUEUE = "send-vote-paper-upload-notification-dev";


    /**
     * @param votePaperRegisterEvent {@link VotePaperRegisterEvent}
     * @return {@link SendResult}
     * @see com.tune_fun.v1.vote.adapter.input.message.VoteMessageConsumer#consumeVotePaperUploadEvent(VotePaperRegisterEvent)
     */
    @Override
    public SendResult<?> produceVotePaperUploadEvent(final VotePaperRegisterEvent votePaperRegisterEvent) {
        return sqsProvider.sendMessageRangedQueue(VOTE_PAPER_UPLOAD_QUEUE, votePaperRegisterEvent);
    }

    @Override
    public SendResult<?> produceVotePaperUpdateDeliveryDateEvent(final VotePaperUpdateDeliveryDateEvent votePaperUpdateDeliveryDateEvent) {
        return null;
    }
}
