package com.tune_fun.v1.common.aspect;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.lock.DistributedTransactionMediator;
import com.tune_fun.v1.common.lock.DistributionLock;
import com.tune_fun.v1.common.lock.DistributionLockKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributionLockAspect {
    private static final String LOCK_PREFIX = "LOCK: ";

    private final RedissonClient redissonClient;
    private final DistributedTransactionMediator distributedTransactionMediator;

    @Around("@annotation(distributionLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributionLock distributionLock) throws Throwable {
        LockInfo lockInfo = getResult(joinPoint, distributionLock);

        try {
            if (!lockInfo.reentrantLock().tryLock(distributionLock.waitTime(), distributionLock.leaseTime(), distributionLock.timeUnit()))
                throw CommonApplicationException.LOCK_ACQUISITION_FAILED_ERROR;

            log.info("reentrantLock - {}", lockInfo.key());
            return distributedTransactionMediator.proceed(joinPoint);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw CommonApplicationException.LOCK_INTERRUPTED_ERROR;
        } finally {
            log.info("unlock - {}", lockInfo.key());
            lockInfo.reentrantLock().unlock();
        }
    }

    private LockInfo getResult(ProceedingJoinPoint joinPoint, DistributionLock distributionLock) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String key = LOCK_PREFIX + DistributionLockKeyGenerator.generate(methodSignature.getName(),
                methodSignature.getParameterNames(), joinPoint.getArgs(), distributionLock.key());
        RLock lock = redissonClient.getLock(key);
        return new LockInfo(key, lock);
    }

    private record LockInfo(String key, RLock reentrantLock) {
    }

}
