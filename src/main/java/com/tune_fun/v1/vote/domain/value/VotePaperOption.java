package com.tune_fun.v1.vote.domain.value;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VotePaperOption {
    ALLOW_ADD_CHOICES("allow-add-choices", "참여자가 직접 추가"), DENY_ADD_CHOICES("deny-add-choices", "아티스트가 사전 추가");

    @JsonValue private final String value;
    private final String label;

    public static VotePaperOption fromValue(final String value) {
        for (final VotePaperOption option : VotePaperOption.values()) if (option.value.equals(value)) return option;

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
