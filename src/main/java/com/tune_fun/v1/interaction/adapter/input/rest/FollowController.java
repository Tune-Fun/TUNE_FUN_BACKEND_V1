package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.interaction.application.port.input.command.InteractionCommands;
import com.tune_fun.v1.interaction.application.port.input.query.FollowingQuery;
import com.tune_fun.v1.interaction.application.port.input.usecase.FollowUserUseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnFollowUserUseCase;
import com.tune_fun.v1.interaction.domain.ScrollableFollowInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequiredArgsConstructor
public class FollowController {

    private final ResponseMapper responseMapper;

    private final FollowUserUseCase followUserUseCase;
    private final UnFollowUserUseCase unFollowUserUseCase;

    private final FollowingQuery followingQuery;

    @PostMapping(value = Uris.FOLLOW_ROOT)
    public void follow(final InteractionCommands.Follow command, final User user) {
        followUserUseCase.follow(command, user);
        responseMapper.ok();
    }

    @DeleteMapping(value = Uris.FOLLOW_ROOT)
    public void unfollow(final InteractionCommands.UnFollow command, final User user) {
        unFollowUserUseCase.unfollow(command, user);
        responseMapper.ok();
    }

    @GetMapping(value = Uris.FOLLOWING)
    public ResponseEntity<Response<ScrollableFollowInfoResponse>> fetchFollowingUser(@RequestParam(name = "last_id", required = false) Integer lastId, final User user) {
        Window<ScrollableFollowInfo> followInfos = followingQuery.fetchFollowingUser(lastId, user);
        ScrollableFollowInfoResponse response = new ScrollableFollowInfoResponse(followInfos);
        return responseMapper.ok(response);
    }

}
