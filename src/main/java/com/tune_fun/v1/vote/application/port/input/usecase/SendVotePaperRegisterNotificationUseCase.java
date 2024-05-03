package com.tune_fun.v1.vote.application.port.input.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;

public interface SendVotePaperRegisterNotificationUseCase {
    void send(final VotePaperRegisterEvent votePaperRegisterEvent) throws JsonProcessingException, FirebaseMessagingException;
}
