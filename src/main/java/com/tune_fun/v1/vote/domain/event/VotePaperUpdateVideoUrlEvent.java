package com.tune_fun.v1.vote.domain.event;

public record VotePaperUpdateVideoUrlEvent(String id, String author, String title, String content, String videoUrl) {
}
