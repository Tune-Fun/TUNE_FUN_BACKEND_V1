package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UpdateVotePaperStatisticsUseCase;
import com.tune_fun.v1.interaction.application.port.output.LoadVotePaperLikeCountPort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePaperStatisticsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@UseCase
@RequiredArgsConstructor
public class UpdateVotePaperStatisticsService implements UpdateVotePaperStatisticsUseCase {

    private final LoadVotePaperLikeCountPort loadVotePaperLikeCountPort;
    private final SaveVotePaperStatisticsPort saveVotePaperStatPort;


    @Override
    public void updateVotePaperStatistics() {
        Set<String> keys = loadVotePaperLikeCountPort.getVotePaperLikeCountKeys();

        keys.forEach(key -> {
            Long likeCount = loadVotePaperLikeCountPort.getVotePaperLikeCount(key);
            Long votePaperId = loadVotePaperLikeCountPort.getVotePaperId(key);
            saveVotePaperStatPort.updateLikeCount(votePaperId, likeCount);
        });
    }
}
