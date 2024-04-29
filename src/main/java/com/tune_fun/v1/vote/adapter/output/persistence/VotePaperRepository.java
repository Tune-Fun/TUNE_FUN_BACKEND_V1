package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VotePaperRepository extends JpaRepository<VotePaperJpaEntity, String> {

    @EntityGraph(attributePaths = {"author"})
    Optional<VotePaperJpaEntity> findByVoteEndAtAfterAndAuthorUsername(LocalDateTime voteEndAt, String author);

    Optional<VotePaperJpaEntity> findByVoteEndAtAfterAndId(LocalDateTime voteEndAt, Long id);

}