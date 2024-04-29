package com.tune_fun.v1.vote.application.port.input.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperRegisterEvent;

public interface SendVotePaperRegisterFcmUseCase {
    void send(final ProduceVotePaperRegisterEvent produceVotePaperRegisterEvent) throws JsonProcessingException, FirebaseMessagingException;
}
