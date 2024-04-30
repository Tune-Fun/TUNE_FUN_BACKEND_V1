package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;

@FunctionalInterface
public interface ProduceVotePaperUpdateDeliveryDateEventPort {

    void produceVotePaperUpdateDeliveryDateEvent(final VotePaperUpdateDeliveryDateEvent votePaperUpdateDeliveryDateEvent);

}
