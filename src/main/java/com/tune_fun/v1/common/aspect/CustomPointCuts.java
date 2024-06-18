package com.tune_fun.v1.common.aspect;

import org.aspectj.lang.annotation.Pointcut;

public final class CustomPointCuts {

    @Pointcut("execution(* org.springdoc.webmvc.api.*.*(..))")
    public void swaggerUI() {
    }

    @Pointcut("execution(* org.springdoc.webmvc.ui.*.*(..))")
    public void springdoc() {
    }

    @Pointcut("execution(* com.tune_fun.v1.common.api.HealthCheckController.*(..))")
    public void healthCheck() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    @Pointcut(
            "within(@org.springframework.stereotype.Repository *)" +
                    " || within(@org.springframework.stereotype.Service *)" +
                    " || within(@org.springframework.web.bind.annotation.RestController *)"
    )
    public void springBeanPointcut() {
    }

    @Pointcut(
            "within(@com.tune_fun.v1.common.stereotype.PersistenceAdapter *)" +
                    " || within(@com.tune_fun.v1.common.stereotype.UseCase *)" +
                    " || within(@com.tune_fun.v1.common.stereotype.WebAdapter *)"
    )
    public void hexagon() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getApi() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postApi() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putApi() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchApi() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteApi() {
    }

    @Pointcut("getApi() || postApi() || putApi() || patchApi() || deleteApi()")
    public void restApi() {
    }

    @Pointcut("execution(* com.tune_fun.v1.*.adapter.output.persistence.*.*(..))")
    public void persistence() {
    }

    @Pointcut("execution(* com.tune_fun.v1.*.application.port.input.usecase.*.*(..))")
    public void useCase() {
    }

    @Pointcut("execution(* com.tune_fun.v1.common.*.*(..))")
    public void common() {
    }

}
