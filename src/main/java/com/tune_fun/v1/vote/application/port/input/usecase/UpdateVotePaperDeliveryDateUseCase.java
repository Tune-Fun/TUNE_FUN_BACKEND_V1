package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import org.springframework.security.core.userdetails.User;

@FunctionalInterface
public interface UpdateVotePaperDeliveryDateUseCase {

    void updateDeliveryDate(final Long votePaperId, final VotePaperCommands.UpdateDeliveryDate command, final User user);

}
