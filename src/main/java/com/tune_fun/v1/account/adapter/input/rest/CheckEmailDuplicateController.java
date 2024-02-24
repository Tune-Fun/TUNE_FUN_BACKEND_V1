package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.CheckEmailDuplicateUseCase;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.tune_fun.v1.common.response.MessageCode.SUCCESS_EMAIL_UNIQUE;

@RestController
@WebAdapter
@Validated
@RequiredArgsConstructor
public class CheckEmailDuplicateController {

    private final CheckEmailDuplicateUseCase checkEmailDuplicateUseCase;
    private final ResponseMapper responseMapper;

    @GetMapping(value = Uris.CHECK_EMAIL_DUPLICATE)
    public ResponseEntity<Response<BasePayload>> checkUsernameDuplicate(@RequestParam(name = "email")
                                                                            @NotBlank(message = "{email.not_blank}") final String email) {
        checkEmailDuplicateUseCase.checkEmailDuplicate(email);
        return responseMapper.ok(SUCCESS_EMAIL_UNIQUE);
    }

}
