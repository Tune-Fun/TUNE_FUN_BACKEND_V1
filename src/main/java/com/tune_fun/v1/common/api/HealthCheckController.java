package com.tune_fun.v1.common.api;

import com.tune_fun.v1.common.config.Uris;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping(value = Uris.HEALTH_CHECK)
    public String healthCheck() {
        return "OK";
    }

}