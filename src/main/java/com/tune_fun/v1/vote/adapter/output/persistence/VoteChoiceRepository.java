package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteChoiceRepository extends JpaRepository<VoteChoiceJpaEntity, Long> {

    @EntityGraph(attributePaths = {"votePaper"})
    List<VoteChoiceJpaEntity> findAllByVotePaperId(final Long votePaperId);

}
