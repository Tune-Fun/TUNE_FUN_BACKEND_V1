package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.SetNewPasswordUseCase;
import com.tune_fun.v1.account.domain.state.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.tune_fun.v1.common.response.MessageCode.SUCCESS_SET_NEW_PASSWORD;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class SetNewPasswordController {

    private final SetNewPasswordUseCase setNewPasswordUseCase;
    private final ResponseMapper responseMapper;

    @PatchMapping(value = Uris.SET_NEW_PASSWORD)
    public ResponseEntity<Response<BasePayload>> setNewPassword(@Valid @RequestBody AccountCommands.SetNewPassword command,
                                                                @CurrentUser User user) throws Exception {
        setNewPasswordUseCase.setNewPassword(command, user);
        return responseMapper.ok(SUCCESS_SET_NEW_PASSWORD);
    }

}
