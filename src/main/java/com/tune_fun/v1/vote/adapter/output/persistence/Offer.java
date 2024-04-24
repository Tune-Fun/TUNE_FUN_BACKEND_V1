package com.tune_fun.v1.vote.adapter.output.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class Offer {
    private String name;
    private String artistName;
    private List<String> genres;
    private Integer durationMs;
    private String releaseDate;
}
