package com.tune_fun.v1.common.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ErrorLogTestController {

    @GetMapping("/error")
    public String hello() {
        log.error("error log test");
        return "error log test";
    }

}
