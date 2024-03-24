package com.tune_fun.v1.base;

import com.tune_fun.v1.base.annotation.IntegrationTest;
import com.tune_fun.v1.external.aws.sqs.TestConsumer;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public abstract class AbstractIntegrationTest {

    @SpyBean
    protected TestConsumer testConsumer;

}
