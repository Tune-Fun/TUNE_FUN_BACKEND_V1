package com.habin.demo.common.aspect;

import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.rate.TokenBucketResolver;
import com.habin.demo.common.response.MessageCode;
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

    @Around("com.habin.demo.common.aspect.PointCuts.restController() " +
            "&& !com.habin.demo.common.aspect.PointCuts.swaggerUI()" +
            "&& !com.habin.demo.common.aspect.PointCuts.springdoc() ")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Call rateLimit : {}", joinPoint.getClass());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String key = RATE_LIMIT_PREFIX + methodSignature.getName();

        if (tokenBucketResolver.checkBucketCounter(key)) return joinPoint.proceed();

        throw new CommonApplicationException(MessageCode.TOO_MANY_REQUESTS);
    }

}
