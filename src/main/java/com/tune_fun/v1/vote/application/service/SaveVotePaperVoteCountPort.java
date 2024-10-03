package com.tune_fun.v1.vote.application.service;

public interface SaveVotePaperVoteCountPort {
    void incrementVotePaperVoteCount(final Long votePaperId, final Long voteChoiceId);
}
