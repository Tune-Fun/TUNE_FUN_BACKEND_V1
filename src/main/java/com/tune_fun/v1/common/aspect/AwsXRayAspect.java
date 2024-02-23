package com.tune_fun.v1.common.aspect;

import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import com.amazonaws.xray.spring.aop.XRaySpringDataInterceptor;
import com.tune_fun.v1.common.config.annotation.NotAllowTest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@NotAllowTest
@Aspect
@Component
public class AwsXRayAspect extends AbstractXRayInterceptor {

    @Override
    protected Map<String, Map<String, Object>> generateMetadata(ProceedingJoinPoint proceedingJoinPoint, Subsegment subsegment) {
        return super.generateMetadata(proceedingJoinPoint, subsegment);
    }

    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && (bean(*Controller) || bean(*Service))")
    public void xrayEnabledClasses() {}

    @Bean
    public XRaySpringDataInterceptor xRaySpringDataInterceptor() {
        return new XRaySpringDataInterceptor();
    }
}
