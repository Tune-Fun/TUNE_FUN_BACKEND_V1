package com.tune_fun.v1.vote.adapter.input.choreography;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@XRayEnabled
@Component
@RequiredArgsConstructor
public class VoteKafkaConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;

}
