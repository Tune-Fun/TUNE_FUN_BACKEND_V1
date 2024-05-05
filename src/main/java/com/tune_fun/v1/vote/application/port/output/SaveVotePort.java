package com.tune_fun.v1.vote.application.port.output;

public interface SaveVotePort {
    void saveVote(final Long voteChoiceId, final String username);
}
