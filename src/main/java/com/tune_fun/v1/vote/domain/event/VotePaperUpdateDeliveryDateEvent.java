package com.tune_fun.v1.vote.domain.event;

public record VotePaperUpdateDeliveryDateEvent(String id, String author, String title, String content) {
}
