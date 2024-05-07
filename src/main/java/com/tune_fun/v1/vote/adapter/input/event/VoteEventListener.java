package com.tune_fun.v1.vote.adapter.input.event;

import com.tune_fun.v1.vote.application.port.input.usecase.ScheduleVotePaperDeadlineActionUseCase;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class VoteEventListener {

    private final ScheduleVotePaperDeadlineActionUseCase scheduleVotePaperDeadlineActionUseCase;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleVotePaperRegisterEvent(VotePaperRegisterEvent event) {
        scheduleVotePaperDeadlineActionUseCase.scheduleVotePaperDeadlineAction(event);
    }

}
