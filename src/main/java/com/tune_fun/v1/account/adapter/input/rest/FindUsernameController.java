package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.FindUsernameUseCase;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class FindUsernameController {

    private final FindUsernameUseCase findUsernameUseCase;
    private final ResponseMapper responseMapper;

    @GetMapping(value = Uris.FIND_USERNAME)
    public ResponseEntity<Response<?>> findUsername(@RequestParam(name = "email") final String email) throws Exception {
        findUsernameUseCase.findUsername(email);
        return responseMapper.ok();
    }

}
