package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;

import java.util.Set;

public interface SaveVoteChoicePort {
    void saveVoteChoice(final Long votePaperId, final Set<SaveVoteChoice> behavior);
}
