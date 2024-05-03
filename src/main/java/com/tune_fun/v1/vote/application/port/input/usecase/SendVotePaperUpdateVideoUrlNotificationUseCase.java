package com.tune_fun.v1.vote.application.port.input.usecase;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateVideoUrlEvent;

@FunctionalInterface
public interface SendVotePaperUpdateVideoUrlNotificationUseCase {
    void send(final VotePaperUpdateVideoUrlEvent event) throws FirebaseMessagingException;
}
