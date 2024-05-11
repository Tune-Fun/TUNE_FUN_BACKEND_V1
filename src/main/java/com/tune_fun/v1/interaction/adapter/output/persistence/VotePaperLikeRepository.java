package com.tune_fun.v1.interaction.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VotePaperLikeRepository extends JpaRepository<VotePaperLikeJpaEntity, Long> {
}