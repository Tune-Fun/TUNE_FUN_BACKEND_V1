package com.tune_fun.v1.account.domain.state;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity}
 */
public record CurrentAccount(
        LocalDateTime createdAt,
        LocalDateTime emailVerifiedAt,
        @NotNull @Size(max = 255) String uuid,
        @Size(max = 255) String username,
        String nickname,
        String email,
        List<String> roles) {
}
