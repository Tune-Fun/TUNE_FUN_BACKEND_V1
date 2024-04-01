package com.tune_fun.v1.common.api;

import com.tune_fun.v1.common.config.Uris;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.status;

@RestController
public class OAuth2LoginPageController {

    @GetMapping(value = Uris.NO_AUTH)
    public ResponseEntity<?> noAuth() {
        return status(UNAUTHORIZED).build();
    }

}
