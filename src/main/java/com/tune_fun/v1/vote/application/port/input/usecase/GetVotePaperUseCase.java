package com.tune_fun.v1.vote.application.port.input.usecase;

@FunctionalInterface
public interface GetVotePaperUseCase {

    void getVotePaper(final Long votePaperId);

}
