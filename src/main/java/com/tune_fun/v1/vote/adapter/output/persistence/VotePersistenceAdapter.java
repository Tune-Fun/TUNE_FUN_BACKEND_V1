package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.vote.application.port.output.*;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVote;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
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
        UpdateDeliveryAtPort,
        LoadVoteChoicePort, SaveVoteChoicePort {

    private final AccountPersistenceAdapter accountPersistenceAdapter;

    private final VoteRepository voteRepository;
    private final VotePaperRepository votePaperRepository;
    private final VoteChoiceRepository voteChoiceRepository;

    private final VoteMapper voteMapper;
    private final VotePaperMapper votePaperMapper;
    private final VoteChoiceMapper voteChoiceMapper;

    @Override
    public List<Long> loadVoterIdsByVotePaperUuid(final String uuid) {
        return voteRepository.findVoterIdsByVotePaperUuid(uuid);
    }

    @Override
    public Optional<RegisteredVote> loadVoteByVoterAndVotePaperId(String voter, Long votePaperId) {
        return voteRepository.findByVoterUsernameAndId(voter, votePaperId)
                .map(voteMapper::registeredVote);
    }

    @Override
    public Optional<RegisteredVotePaper> loadRegisteredVotePaper(final String username) {
        return findAvailableVotePaperByAuthor(username)
                .map(votePaperMapper::registeredVotePaper);
    }

    @Override
    public RegisteredVotePaper saveVotePaper(final SaveVotePaper saveVotePaper) {
        AccountJpaEntity account = accountPersistenceAdapter.loadAccountByUsername(saveVotePaper.author())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        VotePaperJpaEntity votePaper = votePaperMapper.fromSaveVotePaperBehavior(saveVotePaper, account);
        VotePaperJpaEntity savedVotePaper = votePaperRepository.save(votePaper);
        return votePaperMapper.registeredVotePaper(savedVotePaper);
    }

    @Override
    public RegisteredVotePaper updateDeliveryAt(final Long votePaperId, final LocalDateTime deliveryAt) {
        VotePaperJpaEntity votePaper = findCompleteVotePaperById(votePaperId)
                .orElseThrow(() -> new IllegalArgumentException("VotePaper not found"));

        VotePaperJpaEntity updatedVotePaper = votePaperMapper.updateDeliveryAt(deliveryAt, votePaper.toBuilder()).build();
        VotePaperJpaEntity savedVotePaper = votePaperRepository.save(updatedVotePaper);
        return votePaperMapper.registeredVotePaper(savedVotePaper);
    }

    @Override
    public void saveVoteChoice(final Long votePaperId, final Set<SaveVoteChoice> behavior) {
        VotePaperJpaEntity votePaperJpaEntity = findProgressingVotePaperById(votePaperId)
                .orElseThrow(() -> new IllegalArgumentException("VotePaper not found"));

        Set<VoteChoiceJpaEntity> voteChoices = voteChoiceMapper.fromSaveVoteChoiceBehaviors(behavior);
        Set<VoteChoiceJpaEntity> updatedVoteChoices = voteChoices.stream()
                .map(voteChoice -> voteChoiceMapper.updateVoteChoice(votePaperJpaEntity, voteChoice.toBuilder()).build())
                .collect(toSet());

        voteChoiceRepository.saveAll(updatedVoteChoices);
    }


    public Optional<VotePaperJpaEntity> findProgressingVotePaperById(final Long id) {
        return votePaperRepository.findByVoteEndAtAfterAndId(now(), id);
    }

    public Optional<VotePaperJpaEntity> findCompleteVotePaperById(final Long id) {
        return votePaperRepository.findByVoteEndAtBeforeAndId(now(), id);
    }

    public Optional<VotePaperJpaEntity> findAvailableVotePaperByAuthor(final String username) {
        return votePaperRepository.findByVoteEndAtAfterAndAuthorUsername(now(), username);
    }

    @Override
    public List<RegisteredVoteChoice> loadRegisteredVoteChoice(Long votePaperId) {
        List<VoteChoiceJpaEntity> voteChoices = voteChoiceRepository.findAllByVotePaperId(votePaperId);
        return voteChoiceMapper.registeredVoteChoices(voteChoices);
    }
}
