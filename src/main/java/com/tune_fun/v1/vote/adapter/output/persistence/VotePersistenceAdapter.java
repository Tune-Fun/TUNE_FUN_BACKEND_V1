package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.vote.application.port.output.*;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toSet;

@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class VotePersistenceAdapter implements
        LoadVotePort, SaveVotePort,
        LoadVotePaperPort, SaveVotePaperPort,
        SaveVoteChoicePort {

    private final VoteRepository voteRepository;
    private final VotePaperRepository votePaperRepository;
    private final VoteChoiceRepository voteChoiceRepository;

    private final VotePaperMapper votePaperMapper;

    @Override
    public Optional<RegisteredVotePaper> loadRegisteredVotePaper(final String username) {
        return findAvailableVotePaperByAuthor(username)
                .map(votePaperMapper::registeredVotePaper);
    }

    @Override
    public RegisteredVotePaper saveVotePaper(final SaveVotePaper saveVotePaper) {
        VotePaperMongoEntity votePaper = votePaperMapper.fromSaveVotePaperBehavior(saveVotePaper);
        VotePaperMongoEntity savedVotePaper = votePaperRepository.save(votePaper);
        return votePaperMapper.registeredVotePaper(savedVotePaper);
    }

    @Override
    public void saveVoteChoice(final String votePaperId, final Set<SaveVoteChoice> behavior) {
        VotePaperMongoEntity votePaperMongoEntity = findAvailableVotePaperById(votePaperId).get();

        Set<VoteChoiceMongoEntity> voteChoices = votePaperMapper.fromSaveVoteChoiceBehaviors(behavior);
        Set<VoteChoiceMongoEntity> updatedVoteChoices = voteChoices.stream()
                .map(voteChoice -> votePaperMapper.updateVotePaper(votePaperMongoEntity, voteChoice.toBuilder()).build())
                .collect(toSet());

        voteChoiceRepository.saveAll(updatedVoteChoices);
    }

    public Optional<VotePaperMongoEntity> findAvailableVotePaperById(final String id) {
        return votePaperRepository.findByVoteEndAtBeforeAndId(now(), id);
    }

    public Optional<VotePaperMongoEntity> findAvailableVotePaperByAuthor(final String username) {
        return votePaperRepository.findByVoteEndAtBeforeAndAuthor(now(), username);
    }

}
