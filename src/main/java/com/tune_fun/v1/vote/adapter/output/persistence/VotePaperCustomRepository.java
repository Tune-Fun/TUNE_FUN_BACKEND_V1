package com.tune_fun.v1.vote.adapter.output.persistence;

import java.util.Optional;

public interface VotePaperCustomRepository {

    Optional<VotePaperJpaEntity> findOneAvailable(final Long id, final String username);
}
