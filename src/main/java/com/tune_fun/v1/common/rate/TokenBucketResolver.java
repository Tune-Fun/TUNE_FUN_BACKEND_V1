package com.tune_fun.v1.common.rate;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.tune_fun.v1.common.response.MessageCode.TOO_MANY_REQUESTS;


@Component
@RequiredArgsConstructor
public class TokenBucketResolver {

    private final BucketConfiguration bucketConfiguration;
    private final LettuceBasedProxyManager<byte[]> lettuceBasedProxyManager;

    private Bucket bucket(final String key) {
        return lettuceBasedProxyManager.builder()
                .build(key.getBytes(), () -> bucketConfiguration);
    }

    public boolean checkBucketCounter(String key) {
        if (!bucket(key).tryConsume(1)) throw new CommonApplicationException(TOO_MANY_REQUESTS);
        return true;
    }

}
