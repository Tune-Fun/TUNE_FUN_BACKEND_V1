package com.tune_fun.v1.common.config.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target(ElementType.TYPE)
@NotAllowTest
@Configuration
public @interface OnlyDevelopmentConfiguration {

    @AliasFor(annotation = Configuration.class)
    String value() default "";

    @AliasFor(annotation = Configuration.class)
    boolean proxyBeanMethods() default true;

    @AliasFor(annotation = Configuration.class)
    boolean enforceUniqueMethods() default true;

}
