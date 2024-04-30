package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;

public interface SaveVotePaperPort {
    RegisteredVotePaper saveVotePaper(final SaveVotePaper saveVotePaper);
}
