package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface LoadVotePaperPort {

    Optional<RegisteredVotePaper> loadRegisteredVotePaper(final String username);

    Optional<RegisteredVotePaper> loadRegisteredVotePaper(@NotNull final Long votePaperId);

    Optional<RegisteredVotePaper> loadProgressingOwnedVotePaper(@NotNull final Long votePaperId, @NotNull final String username);
}
