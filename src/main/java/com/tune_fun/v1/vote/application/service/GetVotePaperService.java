package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.GetVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class GetVotePaperService implements GetVotePaperUseCase {

    private final LoadVotePaperPort loadVotePaperPort;

    @Override
    public void getVotePaper(final Long votePaperId) {

    }
}
