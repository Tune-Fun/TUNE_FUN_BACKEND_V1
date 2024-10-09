package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.GetVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVoteChoicePort;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperStatisticsPort;
import com.tune_fun.v1.vote.application.port.output.LoadVotePort;
import com.tune_fun.v1.vote.domain.value.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@UseCase
@RequiredArgsConstructor
public class GetVotePaperService implements GetVotePaperUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final LoadVoteChoicePort loadVoteChoicePort;
    private final LoadVotePort loadVotePort;
    private final LoadVotePaperStatisticsPort loadVotePaperStatisticsPort;

    private final VoteValueMapper voteValueMapper;

    @Override
    public FullVotePaper getVotePaper(final Long votePaperId, final User user) {
        RegisteredVotePaper registeredVotePaper = loadVotePaperPort.loadRegisteredVotePaper(votePaperId)
                .orElseThrow(CommonApplicationException.VOTE_PAPER_NOT_FOUND);
        List<RegisteredVoteChoice> registeredVoteChoices = loadVoteChoicePort.loadRegisteredVoteChoice(votePaperId);
        Optional<RegisteredVote> registeredVote = loadVotePort.loadVoteByVoterAndVotePaperId(user.getUsername(), votePaperId);
        VotePaperStatistics votePaperStatistics = new VotePaperStatistics(votePaperId, loadVotePaperStatisticsPort.getVoteCount(votePaperId), loadVotePaperStatisticsPort.getLikeCount(votePaperId));

        return voteValueMapper.fullVotePaper(registeredVotePaper, registeredVoteChoices, registeredVote.isPresent(), votePaperStatistics);
    }
}
