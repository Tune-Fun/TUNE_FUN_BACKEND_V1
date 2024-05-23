package com.tune_fun.v1.interaction.adapter.input.scheduler;

import com.tune_fun.v1.interaction.application.port.input.usecase.UpdateVotePaperStatisticsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeCountAggregationScheduler {

    private final UpdateVotePaperStatisticsUseCase updateVotePaperStatisticsUseCase;

    @Scheduled(fixedDelay = 1000L * 5L)
    public void aggregateLikeCount() {
        updateVotePaperStatisticsUseCase.updateVotePaperStatistics();
    }

}
