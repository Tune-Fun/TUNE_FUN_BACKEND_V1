package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.dummy.DummyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
class FollowControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("팔로우, 성공")
    void followSuccess() {
        
    }
}