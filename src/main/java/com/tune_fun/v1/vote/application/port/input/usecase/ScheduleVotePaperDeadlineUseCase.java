package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.domain.event.VotePaperDeadlineEvent;

@FunctionalInterface
public interface ScheduleVotePaperDeadlineUseCase {

    void scheduleVotePaperDeadlineAction(final VotePaperDeadlineEvent event);

}
