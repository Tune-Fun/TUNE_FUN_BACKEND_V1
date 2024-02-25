package com.tune_fun.v1.vote.adapter.output.choreography;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.vote.application.port.output.ProduceVoteUploadEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@XRayEnabled
@Component
@RequiredArgsConstructor
public class VoteChoreographyAdapter implements ProduceVoteUploadEventPort {

    private final KafkaTemplate<String, String> kafkaTemplate;

}
