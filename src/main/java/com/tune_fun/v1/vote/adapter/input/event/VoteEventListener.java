package com.tune_fun.v1.vote.adapter.input.event;

import com.tune_fun.v1.vote.application.port.input.usecase.ScheduleVotePaperDeadlineUseCase;
import com.tune_fun.v1.vote.domain.event.VotePaperDeadlineEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteEventListener {

    private final ScheduleVotePaperDeadlineUseCase scheduleVotePaperDeadlineUseCase;

    @EventListener
    public void handleVotePaperDeadlineEvent(VotePaperDeadlineEvent event) {
        scheduleVotePaperDeadlineUseCase.scheduleVotePaperDeadlineAction(event);
    }

}
