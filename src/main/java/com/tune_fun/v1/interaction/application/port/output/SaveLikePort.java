package com.tune_fun.v1.interaction.application.port.output;

public interface SaveLikePort {
    void saveVotePaperLike(final Long votePaperId, final String username);
}
