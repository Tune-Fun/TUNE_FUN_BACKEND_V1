package com.tune_fun.v1.vote.domain.event;

import java.time.LocalDateTime;

public record VotePaperRegisterEvent(String id, String author, String title, String content, LocalDateTime voteEndAt) {
}
