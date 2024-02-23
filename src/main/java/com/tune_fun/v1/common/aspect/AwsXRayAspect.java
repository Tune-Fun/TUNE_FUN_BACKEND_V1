package com.tune_fun.v1.common.aspect;

import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.BaseAbstractXRayInterceptor;
import com.tune_fun.v1.common.config.annotation.NotAllowTest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@NotAllowTest
@Aspect
@Component
public class AwsXRayAspect extends BaseAbstractXRayInterceptor {


    @Override
    protected Map<String, Map<String, Object>> generateMetadata(ProceedingJoinPoint proceedingJoinPoint, Subsegment subsegment) {
        return super.generateMetadata(proceedingJoinPoint, subsegment);
    }

    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && bean(*Controller)")
    public void xrayEnabledClasses() {}
}
