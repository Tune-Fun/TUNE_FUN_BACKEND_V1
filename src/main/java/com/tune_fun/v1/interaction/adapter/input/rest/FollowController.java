package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.interaction.application.port.input.command.InteractionCommands;
import com.tune_fun.v1.interaction.application.port.input.query.FollowingQuery;
import com.tune_fun.v1.interaction.application.port.input.usecase.FollowUserUseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnFollowUserUseCase;
import com.tune_fun.v1.interaction.domain.ScrollableFollowInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RequiredArgsConstructor
public class FollowController {

    private final ResponseMapper responseMapper;

    private final FollowUserUseCase followUserUseCase;
    private final UnFollowUserUseCase unFollowUserUseCase;

    private final FollowingQuery followingQuery;

    @PostMapping(value = Uris.FOLLOW_ROOT)
    public ResponseEntity<Response<?>> follow(@Valid @RequestBody final InteractionCommands.Follow command, @CurrentUser final User user) {
        followUserUseCase.follow(command, user);
        return responseMapper.ok();
    }

    @DeleteMapping(value = Uris.FOLLOW_ROOT)
    public void unfollow(final InteractionCommands.UnFollow command, @CurrentUser final User user) {
        unFollowUserUseCase.unfollow(command, user);
        responseMapper.ok();
    }

    @GetMapping(value = Uris.FOLLOWING)
    public ResponseEntity<Response<ScrollableFollowInfoResponse>> fetchFollowingUser(@RequestParam(name = "last_id", required = false) Long lastId, @CurrentUser final User user) {
        Window<ScrollableFollowInfo> followInfos = followingQuery.scrollFollowingUser(lastId, user);
        ScrollableFollowInfoResponse response = new ScrollableFollowInfoResponse(followInfos);
        return responseMapper.ok(response);
    }

}
