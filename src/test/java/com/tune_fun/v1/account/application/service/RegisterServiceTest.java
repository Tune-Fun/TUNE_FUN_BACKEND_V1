package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.domain.value.Role;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @InjectMocks
    private RegisterService registerService;

    @Test
    @DisplayName("사용자 등록 - 허용되지 않는 역할이면 USER_POLICY_INVALID_ROLE 예외를 던진다.")
    void testRegister_notAllowedRoleForRegistration() {
        // given
        Role role = Role.ADMIN;

        // when
        Throwable throwable = catchThrowable(() -> registerService.register(role, mock(AccountCommands.Register.class)));

        // then
        assertThat(role.isNotAllowedForRegistration()).isTrue();
        assertThat(throwable).isInstanceOf(CommonApplicationException.class);
    }
}