package com.tune_fun.v1.music.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<MusicJpaEntity, Long> {
}
