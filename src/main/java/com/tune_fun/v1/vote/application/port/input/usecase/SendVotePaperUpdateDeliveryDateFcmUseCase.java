package com.tune_fun.v1.vote.application.port.input.usecase;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;

@FunctionalInterface
public interface SendVotePaperUpdateDeliveryDateFcmUseCase {

    void send(final VotePaperUpdateDeliveryDateEvent event) throws FirebaseMessagingException;

}
