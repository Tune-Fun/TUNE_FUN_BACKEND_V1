package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import io.awspring.cloud.sqs.operations.SendResult;

public interface ProduceVotePaperUploadEventPort {

    SendResult<?> produceVotePaperUploadEvent(final VotePaperRegisterEvent votePaperRegisterEvent);

}
