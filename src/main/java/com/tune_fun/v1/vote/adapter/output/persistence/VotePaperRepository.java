package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VotePaperRepository extends JpaRepository<VotePaperJpaEntity, Long>, VotePaperCustomRepository {

    @EntityGraph(attributePaths = {"author"})
    Optional<VotePaperJpaEntity> findByVoteEndAtAfterAndAuthorUsernameAndEnabledTrue(LocalDateTime voteEndAt, String author);

    @EntityGraph(attributePaths = {"author", "choices"})
    Optional<VotePaperJpaEntity> findByVoteEndAtAfterAndIdAndEnabledTrue(LocalDateTime voteEndAt, Long id);

    Optional<VotePaperJpaEntity> findByVoteEndAtBeforeAndIdAndEnabledTrue(LocalDateTime voteEndAt, Long id);

    @EntityGraph(attributePaths = {"author"})
    Window<VotePaperJpaEntity> findFirst10ByEnabledTrue(KeysetScrollPosition position, Sort sort);

}
