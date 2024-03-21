package com.tune_fun.v1.external.aws.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test_standalone")
public class TestConsumer {

    @SqsListener(value = "test")
    public void consume(final TestMessage message) {
        System.out.println("Received message: " + message);
    }

}
