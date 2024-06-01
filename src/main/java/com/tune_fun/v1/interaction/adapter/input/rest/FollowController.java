package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class FollowController {

    private final ResponseMapper responseMapper;


}
