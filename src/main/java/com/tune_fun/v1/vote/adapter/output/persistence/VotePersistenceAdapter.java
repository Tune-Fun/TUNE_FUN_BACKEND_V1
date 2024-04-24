package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.LoadVotePort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePort;
import com.tune_fun.v1.vote.domain.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.time.LocalDateTime.now;

@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class VotePersistenceAdapter implements
        LoadVotePort, SaveVotePort,
        LoadVotePaperPort, SaveVotePaperPort {

    private final VoteRepository voteRepository;
    private final VotePaperRepository votePaperRepository;
    private final VoteChoiceRepository voteChoiceRepository;

    private final VotePaperMapper votePaperMapper;

    @Override
    public Optional<RegisteredVotePaper> loadRegisteredVotePaper(final String username) {
        return votePaperRepository.findByVoteEndAtBeforeAndAuthor(now(), username)
                .map(votePaperMapper::registeredVotePaper);
    }

}
