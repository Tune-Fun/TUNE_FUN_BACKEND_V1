package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<VoteJpaEntity, Long>, VoteCustomRepository {

    @EntityGraph(attributePaths = {"voteChoice", "voter", "votePaper"})
    Optional<VoteJpaEntity> findByVoterUsernameAndId(final String voter, final Long votePaperId);
}
