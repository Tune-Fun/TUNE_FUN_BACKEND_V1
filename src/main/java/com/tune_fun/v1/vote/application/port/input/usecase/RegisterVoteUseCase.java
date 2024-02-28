package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.application.port.input.command.VoteCommands;

@FunctionalInterface
public interface RegisterVoteUseCase {

    void register(final VoteCommands.Register command);

}
