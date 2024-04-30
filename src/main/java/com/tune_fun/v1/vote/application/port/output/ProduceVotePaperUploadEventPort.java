package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperRegisterEvent;
import io.awspring.cloud.sqs.operations.SendResult;

public interface ProduceVotePaperUploadEventPort {

    SendResult<?> produceVotePaperUploadEvent(final ProduceVotePaperRegisterEvent produceVotePaperRegisterEvent);

}
