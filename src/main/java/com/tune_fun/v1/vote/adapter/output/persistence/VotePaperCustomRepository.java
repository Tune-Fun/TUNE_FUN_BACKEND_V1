package com.tune_fun.v1.vote.adapter.output.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VotePaperCustomRepository {

    Optional<VotePaperJpaEntity> findOneAvailable(final Long id, final String username);

    List<UserInteractedVotePaper> findLikedByUsernameBeforeLastTimeAndLastId(String username, Long lastId, LocalDateTime lastTime, int limit);

    List<Long> findLikedIdsByUsernameAndIds(String username, Set<Long> ids);

    List<UserInteractedVotePaper> findParticipatedByUsernameBeforeLastId(String username, Long lastId, LocalDateTime lastTime, int limit);

    List<Long> findParticipatedVotePaperIdsByUsername(String username, Set<Long> ids);

    List<VotePaperJpaEntity> findRegisteredByUsernameBeforeLastId(String username, Long lastId, LocalDateTime lastTime, int limit);
}
