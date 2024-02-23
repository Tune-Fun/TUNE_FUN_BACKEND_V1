package com.tune_fun.v1.external.aws.xray;

import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class XRayInspector extends AbstractXRayInterceptor {
    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && bean(*Controller)")
    public void xrayEnabledClasses() {}

}
