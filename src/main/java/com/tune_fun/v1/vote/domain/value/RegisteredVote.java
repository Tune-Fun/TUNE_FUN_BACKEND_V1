package com.tune_fun.v1.vote.domain.value;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public final class RegisteredVote {
    private Long id;
    private String uuid;
    private String username;
    private Long votePaperId;
    private String music;
    private String artistName;
}
