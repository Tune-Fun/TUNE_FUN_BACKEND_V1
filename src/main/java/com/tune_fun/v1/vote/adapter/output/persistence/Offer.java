package com.tune_fun.v1.vote.adapter.output.persistence;

import jakarta.persistence.Embeddable;

/**
 * 투표 선택지의 제안 사항
 *
 * @param offerId Spotify Music ID
 * @param music 노래명
 * @param artistName 아티스트
 */
@Embeddable
public record Offer(String offerId, String music, String artistName) {
}
