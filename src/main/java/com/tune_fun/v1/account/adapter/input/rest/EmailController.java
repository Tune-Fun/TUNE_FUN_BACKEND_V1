package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.email.*;
import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.tune_fun.v1.common.response.MessageCode.SUCCESS_EMAIL_UNIQUE;
import static com.tune_fun.v1.common.response.MessageCode.SUCCESS_EMAIL_VERIFIED;

@WebAdapter
@Validated
@RequiredArgsConstructor
public class EmailController {

    private final CheckEmailDuplicateUseCase checkEmailDuplicateUseCase;
    private final CheckEmailVerifiedUseCase checkEmailVerifiedUseCase;

    private final RegisterEmailUseCase registerEmailUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final ChangeEmailUseCase changeEmailUseCase;
    private final UnlinkEmailUseCase unlinkEmailUseCase;

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

    @PostMapping(value = Uris.EMAIL_ROOT)
    public ResponseEntity<?> registerEmail(@Valid @RequestBody AccountCommands.SaveEmail command, @CurrentUser User user) throws Exception {
        registerEmailUseCase.registerEmail(command, user);
        return responseMapper.ok();
    }

    @PostMapping(value = Uris.VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@CurrentUser User user) throws Exception {
        verifyEmailUseCase.verifyEmail(user);
        return responseMapper.ok();
    }

    @PatchMapping(value = Uris.EMAIL_ROOT)
    public ResponseEntity<?> changeEmail(@Valid @RequestBody AccountCommands.SaveEmail command, @CurrentUser User user) {
        changeEmailUseCase.changeEmail(command, user);
        return responseMapper.ok();
    }

    @DeleteMapping(value = Uris.EMAIL_ROOT)
    public ResponseEntity<?> unlinkEmail(@CurrentUser User user) {
        unlinkEmailUseCase.unlinkEmail(user);
        return responseMapper.ok();
    }

}
