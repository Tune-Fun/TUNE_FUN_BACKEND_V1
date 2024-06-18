package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UpdateVotePaperStatisticsUseCase;
import com.tune_fun.v1.interaction.application.port.output.LoadVotePaperLikeCountPort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePaperStatisticsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@UseCase
@RequiredArgsConstructor
public class UpdateVotePaperStatisticsService implements UpdateVotePaperStatisticsUseCase {

    private final LoadVotePaperLikeCountPort loadVotePaperLikeCountPort;
    private final SaveVotePaperStatisticsPort saveVotePaperStatPort;


    @Override
    public void updateVotePaperStatistics() {
        Set<String> keys = loadVotePaperLikeCountPort.getVotePaperLikeCountKeys();
        log.info("Update vote paper statistics for vote paper like count keys: {}", keys);

        keys.forEach(key -> {
            Long likeCount = loadVotePaperLikeCountPort.getVotePaperLikeCount(key);
            Long votePaperId = loadVotePaperLikeCountPort.getVotePaperId(key);
            saveVotePaperStatPort.updateLikeCount(votePaperId, likeCount);
            log.info("Update vote paper statistics for vote paper id: {} with like count: {}", votePaperId, likeCount);
        });

        MDC.clear();
    }
}
