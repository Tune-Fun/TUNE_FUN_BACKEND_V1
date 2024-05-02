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
import com.tune_fun.v1.vote.application.port.input.usecase.ScrollVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.UpdateVotePaperDeliveryDateUseCase;
import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class VotePaperController {

    private final ResponseMapper responseMapper;

    private final ScrollVotePaperUseCase scrollVotePaperUseCase;
    private final RegisterVotePaperUseCase registerVotePaperUseCase;
    private final UpdateVotePaperDeliveryDateUseCase updateVotePaperDeliveryDateUseCase;

    @GetMapping(value = Uris.VOTE_PAPER_ROOT)
    public ResponseEntity<Response<ScrollVotePaperResponse>> getVotePapers(@RequestParam(name = "lastIdx") Integer lastIdx,
                                                                           @RequestParam(required = false, defaultValue = "RECENT") SortType sortType) {
        Window<ScrollableVotePaper> scrollableVotePapers = scrollVotePaperUseCase.scrollVotePaper(lastIdx, sortType.name());
        return responseMapper.ok(MessageCode.SUCCESS, new ScrollVotePaperResponse(scrollableVotePapers));
    }

    // TODO : Follower 로직 구현 후 테스트 재진행 예정
    @PreAuthorize("hasRole('ARTIST')")
    @PostMapping(value = Uris.VOTE_PAPER_ROOT)
    public ResponseEntity<Response<BasePayload>> registerVotePaper(@Valid @RequestBody final VotePaperCommands.Register command,
                                                                   @CurrentUser final User user) throws JsonProcessingException {
        registerVotePaperUseCase.register(command, user);
        return responseMapper.ok(MessageCode.CREATED);
    }

    //    @PreAuthorize("hasRole('ARTIST') && hasPermission(#votePaperId, 'VOTE_PAPER', 'SET_DELIEVERY_DATE')")
    @PreAuthorize("hasRole('ARTIST')")
    @PatchMapping(value = Uris.UPDATE_VOTE_PAPER_DELIVERY_DATE)
    public ResponseEntity<Response<BasePayload>> updateDeliveryDate(@PathVariable("votePaperId") @NotNull(message = "{vote.paper.id.not_null}") final Long votePaperId,
                                                                    @Valid @RequestBody final VotePaperCommands.UpdateDeliveryDate command,
                                                                    @CurrentUser final User user) {
        updateVotePaperDeliveryDateUseCase.updateDeliveryDate(votePaperId, command, user);
        return responseMapper.ok(MessageCode.SUCCESS);
    }

    public enum SortType {
        RECENT, VOTE_COUNT
    }

}
