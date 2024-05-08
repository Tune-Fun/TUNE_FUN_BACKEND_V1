package com.tune_fun.v1.vote.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record VotePaperDeadlineEvent(String id, String author, String title, String content,
                                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime voteEndAt) {
}
