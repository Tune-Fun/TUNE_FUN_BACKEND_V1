package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;

public interface SaveVotePaperPort {
    void saveVotePaper(final SaveVotePaper saveVotePaper);
}
