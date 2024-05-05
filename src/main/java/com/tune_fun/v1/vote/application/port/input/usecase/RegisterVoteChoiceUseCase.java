package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import org.springframework.security.core.userdetails.User;

public interface RegisterVoteChoiceUseCase {
    void registerVoteChoice(final Long votePaperId, final VotePaperCommands.Offer offer, final User user);
}
