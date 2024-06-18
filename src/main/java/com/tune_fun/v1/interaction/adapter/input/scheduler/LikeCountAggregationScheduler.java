package com.tune_fun.v1.interaction.adapter.input.scheduler;

import com.tune_fun.v1.common.config.annotation.NotAllowTest;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.interaction.application.port.input.usecase.UpdateVotePaperStatisticsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NotAllowTest
@RequiredArgsConstructor
public class LikeCountAggregationScheduler {

    private final UpdateVotePaperStatisticsUseCase updateVotePaperStatisticsUseCase;

    @Scheduled(fixedDelay = 1000L * 5L)
    public void aggregateLikeCount() {
        MDC.put("CORRELATION_ID", StringUtil.ulid());
        log.info("Start to aggregate Vote Paper like count");
        updateVotePaperStatisticsUseCase.updateVotePaperStatistics();
    }

}
