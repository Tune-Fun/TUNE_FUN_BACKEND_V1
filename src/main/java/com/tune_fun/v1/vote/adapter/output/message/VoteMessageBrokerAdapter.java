package com.tune_fun.v1.vote.adapter.output.message;

import com.tune_fun.v1.external.aws.sqs.SqsProvider;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUploadEventPort;
import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperUploadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteMessageBrokerAdapter implements ProduceVotePaperUploadEventPort {

    private final SqsProvider sqsProvider;
    private static final String VOTE_PAPER_UPLOAD_QUEUE = "send-vote-paper-upload-notification-dev";


    @Override
    public void produceVotePaperUploadEvent(final ProduceVotePaperUploadEvent produceVotePaperUploadEvent) {
        sqsProvider.sendMessageRangedQueue(VOTE_PAPER_UPLOAD_QUEUE, produceVotePaperUploadEvent);
    }
}
