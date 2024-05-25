package com.tune_fun.v1.vote.domain.value;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public final class RegisteredVotePaperLike {

    private Long id;
    private Long votePaperId;
    private Long likerAccountId;
    private String likerUsername;
    private LocalDateTime createdAt;

}
