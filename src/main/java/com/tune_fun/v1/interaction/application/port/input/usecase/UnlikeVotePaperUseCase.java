package com.tune_fun.v1.interaction.application.port.input.usecase;

@FunctionalInterface
public interface UnlikeVotePaperUseCase {

    void unlikeVotePaper(final Long votePaperId, final Long likeId);

}
