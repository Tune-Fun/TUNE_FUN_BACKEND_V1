package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteChoiceRepository extends JpaRepository<VoteChoiceJpaEntity, Long> {

    @EntityGraph(attributePaths = {"votePaper"})
    List<VoteChoiceJpaEntity> findAllByVotePaperId(final Long votePaperId);

    @EntityGraph(attributePaths = {"votePaper"})
    Optional<VoteChoiceJpaEntity> findByVotePaperIdAndCreatedBy(final Long votePaperId, final String username);
}
