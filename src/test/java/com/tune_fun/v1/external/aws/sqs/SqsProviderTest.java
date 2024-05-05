package com.tune_fun.v1.external.aws.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tune_fun.v1.base.annotation.IntegrationTest;
import com.tune_fun.v1.common.property.EventProperty;
import io.awspring.cloud.sqs.operations.SendResult;
import org.awaitility.core.ThrowingRunnable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;

@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SqsProviderTest {

    @Autowired
    private SqsProvider sut;

    @Autowired
    private EventProperty eventProperty;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SqsAsyncClient sqsAsyncClient;

    @SpyBean
    private TestConsumer testConsumer;

    private static final String QUEUE_NAME = "test";

    @Test
    @Order(1)
    @DisplayName("AWS SQS 메시지 전송, 성공")
    void sendMessageRangedQueue() {
        // given
        assertDoesNotThrow(() -> eventProperty.validateSqsQueueName(QUEUE_NAME));
        TestMessage message = new TestMessage("Hello!");

        // when
        SendResult<?> sendResult = sut.sendMessageRangedQueue(QUEUE_NAME, message);
        assertSame(sendResult.message().getPayload(), message);

        // then
        ThrowingRunnable receiveMessageAssertionRunnable = () ->
                sqsAsyncClient.getQueueUrl(r -> r.queueName(QUEUE_NAME))
                        .thenApply(GetQueueUrlResponse::queueUrl)
                        .thenCompose(queueUrl -> sqsAsyncClient.receiveMessage(buildReceiveMessageRequest(queueUrl)))
                        .thenAccept(r -> {
                                    receiveMessageIsEmpty(r);
                                    receiveMessagePayloadIsSame(r, message);
                                }
                        );
        await().untilAsserted(receiveMessageAssertionRunnable);
        verify(testConsumer).consume(message);
    }

    private static ReceiveMessageRequest buildReceiveMessageRequest(String queueUrl) {
        return ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(1)
                .build();
    }

    private static void receiveMessageIsEmpty(ReceiveMessageResponse r) {
        if (r.messages().isEmpty()) throw new AssertionError("message is empty");
    }

    private void receiveMessagePayloadIsSame(ReceiveMessageResponse r, TestMessage message) {
        try {
            if (r.messages().getFirst().body().equals(objectMapper.writeValueAsString(message)))
                throw new AssertionError("message is not equal");
        } catch (JsonProcessingException e) {
            throw new AssertionError("message not serialized");
        }
    }
}
