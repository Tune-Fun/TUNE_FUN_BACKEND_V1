package com.tune_fun.v1.interaction.adapter.input.scheduler;

import com.tune_fun.v1.interaction.application.port.input.usecase.UpdateVotePaperStatisticsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeCountAggregationScheduler {

    private final UpdateVotePaperStatisticsUseCase updateVotePaperStatisticsUseCase;

    @Scheduled(fixedDelay = 1000L * 5L)
    public void aggregateLikeCount() {
        log.info("Start to aggregate Vote Paper like count");
        updateVotePaperStatisticsUseCase.updateVotePaperStatistics();
    }

}
