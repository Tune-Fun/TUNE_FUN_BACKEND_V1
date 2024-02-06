package com.habin.demo.account.domain.state;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.habin.demo.account.adapter.output.persistence.AccountJpaEntity}
 */
public record CurrentAccount(
        LocalDateTime createdAt,
        @NotNull @Size(max = 255) String uuid,
        @Size(max = 255) String username,
        List<String> roles) {
}