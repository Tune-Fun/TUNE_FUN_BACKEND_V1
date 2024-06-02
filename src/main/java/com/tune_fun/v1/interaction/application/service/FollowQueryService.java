package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.interaction.application.port.input.query.FollowerQuery;
import com.tune_fun.v1.interaction.application.port.input.query.FollowingQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class FollowQueryService implements FollowingQuery, FollowerQuery {


}
