package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.UpdateNicknameUseCase;
import com.tune_fun.v1.account.domain.state.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class UpdateNicknameController {

    private final UpdateNicknameUseCase updateNicknameUsecase;
    private final ResponseMapper responseMapper;

    @PatchMapping(value = Uris.UPDATE_NICKNAME)
    public ResponseEntity<Response<BasePayload>> updateNickname(@Valid @RequestBody final AccountCommands.UpdateNickname command, @CurrentUser final User user) {
        updateNicknameUsecase.updateNickname(command, user);
        return responseMapper.ok(MessageCode.SUCCESS_UPDATE_NICKNAME);
    }

}
