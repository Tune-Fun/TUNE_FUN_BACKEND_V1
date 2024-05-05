package com.tune_fun.v1.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("event")
public record EventProperty(
        Map<String, SqsProducer> sqs,
        Map<String, KafkaProducer> kafka
) {

    public SqsProducer getSqsProducer(final String queueName) {
        return sqs.values().stream()
                .filter(sqsProducer -> sqsProducer.queueName().equals(queueName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid queue name: " + queueName));
    }

    public KafkaProducer getKafkaProducer(final String topic) {
        return kafka.values().stream()
                .filter(kafkaProducer -> kafkaProducer.topic().equals(topic))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid topic: " + topic));
    }

    public void validateSqsQueueName(final String queueName) {
        if (sqs.values().stream().noneMatch(sqsProducer -> sqsProducer.queueName().equals(queueName)))
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
    }

    public void validateKafkaTopic(final String topic) {
        if (kafka.values().stream().noneMatch(kafkaProducer -> kafkaProducer.topic().equals(topic)))
            throw new IllegalArgumentException("Invalid topic: " + topic);
    }

    public record SqsProducer(
            String queueName
    ) {
    }

    public record KafkaProducer(
            String topic
    ) {
    }


}
