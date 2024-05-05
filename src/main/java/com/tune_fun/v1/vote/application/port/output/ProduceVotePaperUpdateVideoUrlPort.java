package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.event.VotePaperUpdateVideoUrlEvent;

@FunctionalInterface
public interface ProduceVotePaperUpdateVideoUrlPort {
    void produceVotePaperUpdateVideoUrlEvent(final VotePaperUpdateVideoUrlEvent votePaperUpdateVideoUrlEvent);
}
