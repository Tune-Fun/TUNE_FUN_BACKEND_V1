package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperUploadEvent;

public interface SendVotePaperRegisterFcmUseCase {
    void send(final ProduceVotePaperUploadEvent produceVotePaperUploadEvent);
}
