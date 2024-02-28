package com.tune_fun.v1.vote.adapter.output.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VotePaperOption {
    ALLOW_ADD_CHOICES("allow-add-choices"), DENY_ADD_CHOICES("deny-add-choices");

    private final String value;
}
