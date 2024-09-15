package com.tune_fun.v1.vote.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.*;
import com.tune_fun.v1.vote.domain.value.FullVotePaper;
import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class VotePaperController {

    private final ResponseMapper responseMapper;

    private final ScrollVotePaperUseCase scrollVotePaperUseCase;

    private final GetVotePaperUseCase getVotePaperUseCase;

    private final RegisterVotePaperUseCase registerVotePaperUseCase;
    private final RegisterVoteChoiceUseCase registerVoteChoiceUseCase;

    private final UpdateVotePaperDeliveryDateUseCase updateVotePaperDeliveryDateUseCase;
    private final UpdateVotePaperVideoUrlUseCase updateVotePaperVideoUrlUseCase;

    private final DeleteVotePaperUseCase deleteVotePaperUseCase;

    @GetMapping(value = Uris.VOTE_PAPER_ROOT)
    public ResponseEntity<Response<ScrollVotePaperResponse>> scrollVotePaper(@RequestParam(name = "last_id") Integer lastId,
                                                                             @RequestParam(name = "sort_type", required = false, defaultValue = "RECENT") SortType sortType,
                                                                             @RequestParam(name = "nickname", required = false) String nickname,
                                                                             @CurrentUser User user) {
        Window<ScrollableVotePaper> scrollableVotePapers = scrollVotePaperUseCase.scrollVotePaper(lastId, sortType.name(), nickname);
        return responseMapper.ok(MessageCode.SUCCESS, new ScrollVotePaperResponse(scrollableVotePapers));
    }

    @GetMapping(value = Uris.MY_VOTE_PAPER_LIKED)
    public ResponseEntity<Response<ScrollVotePaperResponse>> scrollUserLikedVotePaper(
            @RequestParam(name = "last_id", required = false) Long lastId,
            @RequestParam(name = "last_time", required = false) LocalDateTime lastTime,
            @RequestParam(name = "count", required = false) Integer count,
            @CurrentUser User user) {
        Window<ScrollableVotePaper> scrollableVotePapers = scrollVotePaperUseCase.scrollUserLikedVotePaper(user.getUsername(), lastId, lastTime, count);
        return responseMapper.ok(MessageCode.SUCCESS, new ScrollVotePaperResponse(scrollableVotePapers));
    }

    @GetMapping(value = Uris.MY_VOTE_PAPER_VOTED)
    public ResponseEntity<Response<ScrollVotePaperResponse>> scrollUserParticipatedVotePaper(
            @RequestParam(name = "last_id", required = false) Long lastId,
            @RequestParam(name = "last_time", required = false) LocalDateTime lastTime,
            @RequestParam(name = "count", required = false) Integer count,
            @CurrentUser User user) {
        Window<ScrollableVotePaper> scrollableVotePapers = scrollVotePaperUseCase.scrollUserVotedVotePaper(user.getUsername(), lastId, lastTime, count);
        return responseMapper.ok(MessageCode.SUCCESS, new ScrollVotePaperResponse(scrollableVotePapers));
    }

    @PreAuthorize("hasRole('ARTIST')")
    @GetMapping(value = Uris.MY_VOTE_PAPER_REGISTERED)
    public ResponseEntity<Response<ScrollVotePaperResponse>> scrollUserRegisteredVotePaper(
            @RequestParam(name = "last_id", required = false) Long lastId,
            @RequestParam(name = "last_time", required = false) LocalDateTime lastTime,
            @RequestParam(name = "count", required = false) Integer count,
            @CurrentUser User user) {
        Window<ScrollableVotePaper> scrollableVotePapers = scrollVotePaperUseCase.scrollUserRegisteredVotePaper(user.getUsername(), lastId, lastTime, count);
        return responseMapper.ok(MessageCode.SUCCESS, new ScrollVotePaperResponse(scrollableVotePapers));
    }

    @GetMapping(value = Uris.VOTE_PAPER_ROOT + "/{votePaperId}")
    public ResponseEntity<Response<FullVotePaper>> getVotePaper(@PathVariable("votePaperId") @NotNull(message = "{vote.paper.id.not_null}") final Long votePaperId,
                                                                @CurrentUser final User user) {
        FullVotePaper votePaper = getVotePaperUseCase.getVotePaper(votePaperId, user);
        return responseMapper.ok(MessageCode.SUCCESS, votePaper);
    }

    // TODO : Follower 로직 구현 후 테스트 재진행 예정∂
    @PreAuthorize("hasRole('ARTIST')")
    @PostMapping(value = Uris.VOTE_PAPER_ROOT)
    public ResponseEntity<Response<BasePayload>> registerVotePaper(@Valid @RequestBody final VotePaperCommands.Register command,
                                                                   @CurrentUser final User user) throws JsonProcessingException {
        registerVotePaperUseCase.register(command, user);
        return responseMapper.ok(MessageCode.CREATED);
    }

    @PreAuthorize("hasRole('NORMAL')")
    @PostMapping(value = Uris.VOTE_PAPER_CHOICE)
    public ResponseEntity<Response<BasePayload>> registerVotePaperChoice(@PathVariable("votePaperId") @NotNull(message = "{vote.paper.id.not_null}") final Long votePaperId,
                                                                         @Valid @RequestBody final VotePaperCommands.Offer offer,
                                                                         @CurrentUser final User user) {
        registerVoteChoiceUseCase.registerVoteChoice(votePaperId, offer, user);
        return responseMapper.ok(MessageCode.CREATED);
    }

    @PreAuthorize("hasRole('ARTIST')")
    @PatchMapping(value = Uris.UPDATE_VOTE_PAPER_DELIVERY_DATE)
    public ResponseEntity<Response<BasePayload>> updateDeliveryDate(@PathVariable("votePaperId") @NotNull(message = "{vote.paper.id.not_null}") final Long votePaperId,
                                                                    @Valid @RequestBody final VotePaperCommands.UpdateDeliveryDate command,
                                                                    @CurrentUser final User user) {
        updateVotePaperDeliveryDateUseCase.updateDeliveryDate(votePaperId, command, user);
        return responseMapper.ok(MessageCode.SUCCESS);
    }

    @PreAuthorize("hasRole('ARTIST')")
    @PatchMapping(value = Uris.UPDATE_VOTE_PAPER_VIDEO_URL)
    public ResponseEntity<Response<BasePayload>> updateVideoUrl(@PathVariable("votePaperId") @NotNull(message = "{vote.paper.id.not_null}") final Long votePaperId,
                                                                @Valid @RequestBody final VotePaperCommands.UpdateVideoUrl command,
                                                                @CurrentUser final User user) {
        updateVotePaperVideoUrlUseCase.updateVideoUrl(votePaperId, command, user);
        return responseMapper.ok(MessageCode.SUCCESS);
    }

    @PreAuthorize("hasRole('ARTIST')")
    @DeleteMapping(value = Uris.VOTE_PAPER_ROOT)
    public ResponseEntity<Response<BasePayload>> deleteVotePaper(@RequestParam(name = "vote_paper_id") @NotNull(message = "{vote.paper.id.not_null}") Long votePaperId,
                                                                 @CurrentUser User user) {
        deleteVotePaperUseCase.delete(votePaperId, user);
        return responseMapper.ok(MessageCode.SUCCESS);
    }

    public enum SortType {
        RECENT, VOTE_COUNT
    }

}
