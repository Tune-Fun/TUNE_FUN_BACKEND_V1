package com.tune_fun.v1.interaction.domain;

import com.tune_fun.v1.common.response.BasePayload;

/**
 *
 * @param id 사용자 계정 식별자
 * @param username 사용자 아이디
 * @param nickname 사용자 닉네임
 * @param profileImageUrl 사용자 프로필 이미지 URL
 */
public record ScrollableArtist(Long id,
                               String username,
                               String nickname,
                               String profileImageUrl) implements BasePayload  {
}
