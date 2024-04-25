package com.tune_fun.v1.vote.adapter.output.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class Offer {
    private String name;
    private String artistName;
    private Set<String> genres;
    private String releaseDate;
    private Integer durationMs;
}
