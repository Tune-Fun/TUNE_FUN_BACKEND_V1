package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.query.AccountQueries;
import com.tune_fun.v1.account.application.port.input.usecase.FindUsernameUseCase;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class FindUsernameController {

    private final FindUsernameUseCase findUsernameUseCase;
    private final ResponseMapper responseMapper;

    @PostMapping(value = Uris.FIND_USERNAME)
    public ResponseEntity<Response<?>> findUsername(@Valid @RequestBody AccountQueries.Username query) throws Exception {
        findUsernameUseCase.findUsername(query);
        return responseMapper.ok();
    }

}
