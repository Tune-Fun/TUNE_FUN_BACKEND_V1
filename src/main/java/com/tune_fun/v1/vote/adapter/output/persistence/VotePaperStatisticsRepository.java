package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VotePaperStatisticsRepository extends JpaRepository<VotePaperStatisticsJpaEntity, Long>, VotePaperStatisticsCustomRepository {
}