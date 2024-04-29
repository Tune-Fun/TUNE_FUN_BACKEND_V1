package com.tune_fun.v1.vote.adapter.output.persistence;

import java.util.Set;

public record Offer(String music, String artistName, Set<String> genres, String releaseDate, Integer durationMs) {
}
