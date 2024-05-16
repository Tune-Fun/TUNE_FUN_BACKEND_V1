package com.tune_fun.v1.interaction.application.port.output;

public interface SaveVotePaperLikeCountPort {

    void incrementVotePaperLikeCount(final Long votePaperId);

    void decrementVotePaperLikeCount(final Long votePaperId);

}
