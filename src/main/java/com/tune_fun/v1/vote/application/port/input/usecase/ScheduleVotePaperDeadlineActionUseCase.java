package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;

@FunctionalInterface
public interface ScheduleVotePaperDeadlineActionUseCase {

    void scheduleVotePaperDeadlineAction(final VotePaperRegisterEvent event);

}
