package com.tune_fun.v1.vote.adapter.output.storage;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.external.aws.s3.S3Mediator;
import com.tune_fun.v1.vote.application.port.output.SaveVoteImagePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@XRayEnabled
@Component
@RequiredArgsConstructor
public class VoteStorageAdapter implements SaveVoteImagePort {

    private final S3Mediator s3Mediator;

}
