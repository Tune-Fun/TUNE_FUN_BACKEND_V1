package com.tune_fun.v1.vote.adapter.output.message;

import com.tune_fun.v1.external.aws.sqs.SqsProvider;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUploadEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteMessageBrokerAdapter implements ProduceVotePaperUploadEventPort {

    private final SqsProvider sqsProvider;

}
