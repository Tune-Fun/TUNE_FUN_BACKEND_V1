package com.tune_fun.v1.vote.application.port.output;

public interface LoadVotePaperStatisticsPort {
    Long getLikeCount(final Long votePaperId);
}
