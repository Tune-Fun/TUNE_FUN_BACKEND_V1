package com.tune_fun.v1.vote.adapter.output.storage;

import com.tune_fun.v1.external.aws.s3.S3Mediator;
import com.tune_fun.v1.vote.application.port.output.SaveVoteImagePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VoteStorageAdapter implements SaveVoteImagePort {

    private final S3Mediator s3Mediator;

}
