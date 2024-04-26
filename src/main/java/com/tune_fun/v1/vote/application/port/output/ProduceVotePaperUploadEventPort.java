package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperUploadEvent;

public interface ProduceVotePaperUploadEventPort {

    void produceVotePaperUploadEvent(final ProduceVotePaperUploadEvent produceVotePaperUploadEvent);

}
