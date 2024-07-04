package com.tune_fun.v1.vote.domain.event;

public record VotePaperRegisterEvent(Long id, String uuid, Long authorId, String author, String title, String content) {
}