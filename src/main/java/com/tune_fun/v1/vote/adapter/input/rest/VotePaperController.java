package com.tune_fun.v1.vote.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.UpdateVotePaperDeliveryDateUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class VotePaperController {

    private final ResponseMapper responseMapper;

    private final RegisterVotePaperUseCase registerVotePaperUseCase;
    private final UpdateVotePaperDeliveryDateUseCase updateVotePaperDeliveryDateUseCase;

    // TODO : Follower 로직 구현 후 테스트 재진행 예정
    @PreAuthorize("hasRole('ARTIST')")
    @PostMapping(value = Uris.REGISTER_VOTE_PAPER)
    public ResponseEntity<Response<BasePayload>> registerVotePaper(@Valid @RequestBody final VotePaperCommands.Register command,
                                                                   @CurrentUser final User user) throws JsonProcessingException {
        registerVotePaperUseCase.register(command, user);
        return responseMapper.ok(MessageCode.CREATED);
    }

    // TODO : Vote 로직 구현 후 테스트 재진행 예정
    @PreAuthorize("hasRole('ARTIST') && hasPermission(#command.votePaperId(), 'VOTE_PAPER', 'SET_DELIEVERY_DATE')")
    @PatchMapping(value = Uris.SET_VOTE_PAPER_DELIVERY_DATE)
    public ResponseEntity<Response<BasePayload>> updateDeliveryDate(@Valid @RequestBody final VotePaperCommands.UpdateDeliveryDate command,
                                                                    @CurrentUser final User user) throws JsonProcessingException {
        updateVotePaperDeliveryDateUseCase.updateDeliveryDate(command);
        return responseMapper.ok(MessageCode.SUCCESS);
    }

}
