package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.domain.value.FullVotePaper;

@FunctionalInterface
public interface GetVotePaperUseCase {

    FullVotePaper getVotePaper(final Long votePaperId);

}
