package com.tune_fun.v1.vote.adapter.output.message;

import com.tune_fun.v1.external.aws.sqs.SqsProvider;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUpdateDeliveryDateEventPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUploadEventPort;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import io.awspring.cloud.sqs.operations.SendResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteMessageBrokerAdapter implements
        ProduceVotePaperUploadEventPort, ProduceVotePaperUpdateDeliveryDateEventPort {

    private final SqsProvider sqsProvider;

    @Value("${event.sqs.send-vote-paper-upload-notification.queue-name}")
    private String votePaperUploadQueue;

    @Value("${event.sqs.send-vote-paper-update-delivery-date-notification.queue-name}")
    private String votePaperUpdateDeliveryDateQueue;


    /**
     * @param votePaperRegisterEvent {@link VotePaperRegisterEvent}
     * @return {@link SendResult}
     * @see com.tune_fun.v1.vote.adapter.input.message.VoteMessageConsumer#consumeVotePaperUploadEvent(VotePaperRegisterEvent)
     */
    @Override
    public SendResult<?> produceVotePaperUploadEvent(final VotePaperRegisterEvent votePaperRegisterEvent) {
        return sqsProvider.sendMessageRangedQueue(votePaperUploadQueue, votePaperRegisterEvent);
    }

    /**
     * @param votePaperUpdateDeliveryDateEvent {@link VotePaperUpdateDeliveryDateEvent}
     * @return {@link SendResult}
     */
    @Override
    public SendResult<?> produceVotePaperUpdateDeliveryDateEvent(final VotePaperUpdateDeliveryDateEvent votePaperUpdateDeliveryDateEvent) {
        return sqsProvider.sendMessageRangedQueue(votePaperUpdateDeliveryDateQueue, votePaperUpdateDeliveryDateEvent);
    }
}
