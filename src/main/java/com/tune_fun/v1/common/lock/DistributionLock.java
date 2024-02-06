package com.tune_fun.v1.common.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributionLock {
    String key();

    long waitTime() default 5L;

    long leaseTime() default 2L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
