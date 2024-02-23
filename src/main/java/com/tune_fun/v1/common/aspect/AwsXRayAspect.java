package com.tune_fun.v1.common.aspect;

import com.amazonaws.xray.AWSXRay;
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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@NotAllowTest
@Aspect
@Component
public class AwsXRayAspect extends AbstractXRayInterceptor {

    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && (bean(*Controller) || bean(*Service) || bean(*Adapter))")
    public void xrayEnabledClasses() {}

    @Override
    public Object processXRayTrace(ProceedingJoinPoint pjp) throws Throwable {
        Object process;
        try {
            Subsegment subsegment = AWSXRay.beginSubsegment(generateSubsegmentName(pjp));
            if (subsegment != null) subsegment.setMetadata(this.generateMetadata(pjp, subsegment));

            process = pjp.getArgs().length == 0 ? pjp.proceed() : pjp.proceed(pjp.getArgs());
        } catch (Exception var7) {
            AWSXRay.getCurrentSegmentOptional().ifPresent((x) -> x.addException(var7));
            throw var7;
        } finally {
            log.trace("Ending Subsegment");
            AWSXRay.endSubsegment();
        }

        return process;
    }

    private String generateSubsegmentName(ProceedingJoinPoint pjp){
        String[] declaringTypeName = pjp.getSignature().getDeclaringTypeName().split("\\.");
        if(declaringTypeName.length > 0) {
            return declaringTypeName[declaringTypeName.length - 1] + "." + pjp.getSignature().getName();
        }
        else{
            return pjp.getSignature().getName();
        }
    }

    @Override
    public Map<String, Map<String, Object>> generateMetadata(ProceedingJoinPoint pjp, Subsegment subsegment) {
        final Map<String, Map<String, Object>> metadata = new HashMap<>();
        final Map<String, Object> classInfo = new HashMap<>();
        classInfo.put("Class", pjp.getTarget().getClass().getSimpleName());
        metadata.put("ClassInfo", classInfo);
        return metadata;
    }

    @Bean
    public XRaySpringDataInterceptor xRaySpringDataInterceptor() {
        return new XRaySpringDataInterceptor();
    }
}
