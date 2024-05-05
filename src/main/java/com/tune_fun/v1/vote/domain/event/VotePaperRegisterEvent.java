package com.tune_fun.v1.vote.domain.event;

public record VotePaperRegisterEvent(String id, String author, String title, String content) {
}
