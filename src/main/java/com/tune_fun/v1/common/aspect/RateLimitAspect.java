package com.tune_fun.v1.common.aspect;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.rate.TokenBucketResolver;
import com.tune_fun.v1.common.response.MessageCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    private static final String RATE_LIMIT_PREFIX = "RATE_LIMIT: ";

    private final TokenBucketResolver tokenBucketResolver;

    @Around("com.tune_fun.v1.common.aspect.CustomPointCuts.restController() " +
            "&& !com.tune_fun.v1.common.aspect.CustomPointCuts.swaggerUI()" +
            "&& !com.tune_fun.v1.common.aspect.CustomPointCuts.springdoc() ")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Call rateLimit : {}", joinPoint.getClass());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String key = RATE_LIMIT_PREFIX + methodSignature.getName();

        if (tokenBucketResolver.checkBucketCounter(key)) return joinPoint.proceed();

        throw new CommonApplicationException(MessageCode.TOO_MANY_REQUESTS);
    }

}
