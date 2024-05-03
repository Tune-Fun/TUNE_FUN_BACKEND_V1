package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.common.constant.Constants;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.vote.application.port.output.*;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVote;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toSet;
import static org.springframework.data.domain.ScrollPosition.forward;
import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;

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

    private final VotePaperMapper votePaperMapper;
    private final VoteChoiceMapper voteChoiceMapper;

    @Override
    public List<Long> loadVoterIdsByVotePaperUuid(final String uuid) {
        return voteRepository.findVoterIdsByVotePaperUuid(uuid);
    }

    @Override
    public Optional<RegisteredVote> loadVoteByVoterAndVotePaperId(String voter, Long voteChoiceId) {
        return voteRepository.findByVoterUsernameAndVotePaperId(voter, voteChoiceId);
    }

    @Override
    public void saveVote(Long voteChoiceId, String username) {
        VoteChoiceJpaEntity voteChoice = voteChoiceRepository.findById(voteChoiceId)
                .orElseThrow(() -> new IllegalArgumentException("VoteChoice not found"));

        AccountJpaEntity account = accountPersistenceAdapter.loadAccountByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        VoteJpaEntity vote = VoteJpaEntity.builder()
                .uuid(StringUtil.uuid())
                .voteChoice(voteChoice)
                .voter(account)
                .build();

        voteRepository.save(vote);
    }

    /**
     * TODO : VOTE_COUNT 정렬은 집계 로직 구현 후 추가 예정
     *
     * @param lastId   해당 페이지의 마지막 인덱스
     * @param sortType 정렬 방식
     * @return {@link org.springframework.data.domain.Window} of {@link com.tune_fun.v1.vote.domain.value.ScrollableVotePaper}
     * @see <a href="https://github.com/spring-projects/spring-data-jpa/issues/2996">Keyset-scrolling queries add identifier columns twice when Sort already sorts by Id</a>
     * @see <a href="https://www.baeldung.com/spring-data-jpa-scroll-api">Spring Data JPA Scroll API</a><br>
     */
    @Override
    public Window<ScrollableVotePaper> scrollVotePaper(final Integer lastId, final String sortType) {
        KeysetScrollPosition position = forward(Map.of("id", lastId, "voteEndAt", Constants.LOCAL_DATE_TIME_MIN));
        Sort sort = by(desc("id"), desc("voteEndAt"));
        return votePaperRepository.findFirst10By(position, sort).map(votePaperMapper::scrollableVotePaper);
    }

    @Override
    public Optional<RegisteredVotePaper> loadRegisteredVotePaper(final String username) {
        return findProgressingVotePaperByAuthor(username).map(votePaperMapper::registeredVotePaper);
    }

    @Override
    public Optional<RegisteredVotePaper> loadRegisteredVotePaper(final Long votePaperId) {
        return findProgressingVotePaperById(votePaperId).map(votePaperMapper::registeredVotePaper);
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
        VotePaperJpaEntity votePaper = findOneAvailable(votePaperId, Constants.NULL_STRING)
                .orElseThrow(() -> new IllegalArgumentException("VotePaper not found"));

        VotePaperJpaEntity updatedVotePaper = votePaperMapper.updateDeliveryAt(deliveryAt, votePaper.toBuilder()).build();
        VotePaperJpaEntity savedVotePaper = votePaperRepository.save(updatedVotePaper);
        return votePaperMapper.registeredVotePaper(savedVotePaper);
    }

    @Override
    public List<RegisteredVoteChoice> loadRegisteredVoteChoice(Long votePaperId) {
        List<VoteChoiceJpaEntity> voteChoices = findAllByVotePaperId(votePaperId);
        return voteChoiceMapper.registeredVoteChoices(voteChoices);
    }

    @Override
    public void saveVoteChoice(final Long votePaperId, final Set<SaveVoteChoice> behavior) {
        VotePaperJpaEntity votePaperJpaEntity = findOneAvailable(votePaperId, Constants.NULL_STRING)
                .orElseThrow(() -> new IllegalArgumentException("VotePaper not found"));

        Set<VoteChoiceJpaEntity> voteChoices = voteChoiceMapper.fromSaveVoteChoiceBehaviors(behavior);
        Set<VoteChoiceJpaEntity> updatedVoteChoices = voteChoices.stream()
                .map(voteChoice -> voteChoiceMapper.updateVoteChoice(votePaperJpaEntity, voteChoice.toBuilder()).build())
                .collect(toSet());

        voteChoiceRepository.saveAll(updatedVoteChoices);
    }

    public Optional<VotePaperJpaEntity> findOneAvailable(final Long votePaperId, final String username) {
        return votePaperRepository.findOneAvailable(votePaperId, username);
    }

    public Optional<VotePaperJpaEntity> findCompleteVotePaperById(final Long id) {
        return votePaperRepository.findByVoteEndAtBeforeAndId(now(), id);
    }

    public Optional<VotePaperJpaEntity> findProgressingVotePaperByAuthor(final String username) {
        return votePaperRepository.findByVoteEndAtAfterAndAuthorUsername(now(), username);
    }

    public Optional<VotePaperJpaEntity> findProgressingVotePaperById(final Long id) {
        return votePaperRepository.findByVoteEndAtAfterAndId(now(), id);
    }

    public List<VoteChoiceJpaEntity> findAllByVotePaperId(final Long votePaperId) {
        return voteChoiceRepository.findAllByVotePaperId(votePaperId);
    }
}
