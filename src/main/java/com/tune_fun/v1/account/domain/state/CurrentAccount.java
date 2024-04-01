package com.tune_fun.v1.account.domain.state;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link AccountJpaEntity}
 */
public record CurrentAccount(
        LocalDateTime createdAt,
        LocalDateTime emailVerifiedAt,
        @NotNull @Size(max = 255) String uuid,
        @Size(max = 255) String username,
        String nickname,
        String email,
        Set<String> roles) {
}
