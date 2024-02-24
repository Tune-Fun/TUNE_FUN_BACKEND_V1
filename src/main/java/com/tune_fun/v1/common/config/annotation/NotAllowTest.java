package com.tune_fun.v1.common.config.annotation;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target(ElementType.TYPE)
@Profile("!test & !test_standalone")
public @interface NotAllowTest {
}
