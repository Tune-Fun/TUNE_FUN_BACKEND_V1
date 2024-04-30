package com.tune_fun.v1.external.aws.sqs;

import com.tune_fun.v1.common.property.EventProperty;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsProvider {

    private final SqsTemplate sqsTemplate;
    private final EventProperty eventProperty;

    public SendResult<?> sendMessageRangedQueue(@NotBlank final String queueName, @NotNull final Object message) {
        EventProperty.SqsProducer sqsProducer = eventProperty.getSqsProducer(queueName);

        return sqsTemplate.send(to -> to
                .queue(sqsProducer.queueName())
                .payload(message)
        );
    }

}
