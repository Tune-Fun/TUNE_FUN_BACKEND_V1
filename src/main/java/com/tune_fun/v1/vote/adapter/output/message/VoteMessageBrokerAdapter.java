package com.tune_fun.v1.vote.adapter.output.message;

import com.tune_fun.v1.external.aws.sqs.SqsProvider;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUpdateDeliveryDateEventPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUpdateVideoUrlPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperRegisterEventPort;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateVideoUrlEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteMessageBrokerAdapter implements
        ProduceVotePaperRegisterEventPort, ProduceVotePaperUpdateDeliveryDateEventPort,
        ProduceVotePaperUpdateVideoUrlPort {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final SqsProvider sqsProvider;

    @Value("${event.sqs.send-vote-paper-upload-notification.queue-name}")
    private String votePaperUploadQueue;

    @Value("${event.sqs.send-vote-paper-update-delivery-date-notification.queue-name}")
    private String votePaperUpdateDeliveryDateQueue;

    @Value("${event.sqs.send-vote-paper-update-video-url-notification.queue-name}")
    private String votePaperUpdateVideoUrlQueue;


    /**
     * @param votePaperRegisterEvent {@link VotePaperRegisterEvent}
     *
     * @see com.tune_fun.v1.vote.adapter.input.event.VoteEventListener#handleVotePaperRegisterEvent(VotePaperRegisterEvent)
     * @see com.tune_fun.v1.vote.adapter.input.message.VoteMessageConsumer#consumeVotePaperUploadEvent(VotePaperRegisterEvent)
     */
    @Override
    public void produceVotePaperUploadEvent(final VotePaperRegisterEvent votePaperRegisterEvent) {
        applicationEventPublisher.publishEvent(votePaperRegisterEvent);
        sqsProvider.sendMessageRangedQueue(votePaperUploadQueue, votePaperRegisterEvent);
    }

    /**
     * @param votePaperUpdateDeliveryDateEvent {@link VotePaperUpdateDeliveryDateEvent}
     * @see com.tune_fun.v1.vote.adapter.input.message.VoteMessageConsumer#consumeVotePaperUpdateDeliveryDateEvent(VotePaperUpdateDeliveryDateEvent)
     */
    @Override
    public void produceVotePaperUpdateDeliveryDateEvent(final VotePaperUpdateDeliveryDateEvent votePaperUpdateDeliveryDateEvent) {
        sqsProvider.sendMessageRangedQueue(votePaperUpdateDeliveryDateQueue, votePaperUpdateDeliveryDateEvent);
    }

    /**
     * @param votePaperUpdateVideoUrlEvent {@link VotePaperUpdateDeliveryDateEvent}
     * @see com.tune_fun.v1.vote.adapter.input.message.VoteMessageConsumer#consumeVotePaperUpdateVideoUrlEvent(VotePaperUpdateVideoUrlEvent)
     */
    @Override
    public void produceVotePaperUpdateVideoUrlEvent(final VotePaperUpdateVideoUrlEvent votePaperUpdateVideoUrlEvent) {
        sqsProvider.sendMessageRangedQueue(votePaperUpdateVideoUrlQueue, votePaperUpdateVideoUrlEvent);
    }
}
