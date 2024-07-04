package com.tune_fun.v1.interaction.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.Set;

public interface FollowRepository extends JpaRepository<FollowJpaEntity, Long> {

    Set<FollowJpaEntity> findByFolloweeId(final Long followeeId);

    Optional<FollowJpaEntity> findByFolloweeIdAndFollowerId(final Long followeeId, final Long followerId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteByFolloweeIdAndFollowerId(final Long followeeId, final Long followerId);
}