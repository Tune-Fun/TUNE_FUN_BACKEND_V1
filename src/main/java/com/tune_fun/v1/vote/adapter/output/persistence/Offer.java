package com.tune_fun.v1.vote.adapter.output.persistence;

import jakarta.persistence.Embeddable;

@Embeddable
public record Offer(String music, String artistName, String genres, String releaseDate, Integer durationMs) {
}
