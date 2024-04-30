package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;

import java.util.Optional;

public interface LoadVotePaperPort {

    Optional<RegisteredVotePaper> loadRegisteredVotePaper(final String username);
}
