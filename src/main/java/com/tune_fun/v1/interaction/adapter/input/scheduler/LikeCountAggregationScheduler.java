package com.tune_fun.v1.interaction.adapter.input.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeCountAggregationScheduler {

    private final UpdateVotePaperStatisticsUseCase updateVotePaperStatisticsPort;


    @Scheduled(fixedDelay = 1000L * 5L)
    public void aggregateLikeCount() {

    }

}
