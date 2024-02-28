package com.tune_fun.v1.vote.adapter.output.persistence;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.vote.application.port.output.SaveVotePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@XRayEnabled
@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class VotePersistenceAdapter implements SaveVotePort {

    private final VoteRepository voteRepository;
    private final VotePaperRepository votePaperRepository;
    private final VoteChoiceRepository voteChoiceRepository;

}
