package com.tune_fun.v1.common.api;

import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tune_fun.v1.common.response.MessageCode.SUCCESS;

@RestController
@RequiredArgsConstructor
public class CustomResponseController {

    private final ResponseMapper responseMapper;

    @GetMapping(value = Uris.CUSTOM_RESPONSE_EXAMPLE)
    public ResponseEntity<Response<ExamplePayload>> getCustomResponseExample() {
        return responseMapper.ok(SUCCESS, new ExamplePayload("exampleField"));
    }

    public record ExamplePayload(String exampleField) implements BasePayload {
    }


}
