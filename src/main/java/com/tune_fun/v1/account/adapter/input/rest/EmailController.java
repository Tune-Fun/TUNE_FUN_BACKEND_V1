package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.CheckEmailDuplicateUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.CheckEmailVerifiedUseCase;
import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.tune_fun.v1.common.response.MessageCode.SUCCESS_EMAIL_UNIQUE;
import static com.tune_fun.v1.common.response.MessageCode.SUCCESS_EMAIL_VERIFIED;

@RestController
@WebAdapter
@Validated
@RequiredArgsConstructor
public class EmailController {

    private final CheckEmailDuplicateUseCase checkEmailDuplicateUseCase;
    private final CheckEmailVerifiedUseCase checkEmailVerifiedUseCase;

    private final ResponseMapper responseMapper;

    @GetMapping(value = Uris.CHECK_EMAIL_DUPLICATE)
    public ResponseEntity<Response<BasePayload>> checkUsernameDuplicate(@RequestParam(name = "email")
                                                                        @NotBlank(message = "{email.not_blank}") final String email) {
        checkEmailDuplicateUseCase.checkEmailDuplicate(email);
        return responseMapper.ok(SUCCESS_EMAIL_UNIQUE);
    }

    @GetMapping(value = Uris.CHECK_EMAIL_VERIFIED)
    public ResponseEntity<Response<BasePayload>> checkEmailVerified(@CurrentUser User user) {
        checkEmailVerifiedUseCase.checkEmailVerified(user);
        return responseMapper.ok(SUCCESS_EMAIL_VERIFIED);
    }

}
