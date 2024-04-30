package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import io.awspring.cloud.sqs.operations.SendResult;

@FunctionalInterface
public interface ProduceVotePaperUpdateDeliveryDateEventPort {

    SendResult<?> produceVotePaperUpdateDeliveryDateEvent(final VotePaperUpdateDeliveryDateEvent votePaperUpdateDeliveryDateEvent);

}
