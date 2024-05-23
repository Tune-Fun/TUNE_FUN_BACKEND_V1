package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotePaperStatisticsRepository extends JpaRepository<VotePaperStatisticsJpaEntity, Long>, VotePaperStatisticsCustomRepository {
    Optional<VotePaperStatisticsJpaEntity> findByVotePaperId(final Long votePaperId);
}