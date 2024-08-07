package com.tune_fun.v1.interaction.domain;

import com.tune_fun.v1.common.response.BasePayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class ScrollableArtist implements BasePayload {

    /**
     * 사용자 계정 식별자
     */
    private Long id;

    /**
     * 사용자 아이디
     */
    private String username;

    /**
     * 사용자 닉네임
     */
    private String nickname;

    /**
     * 사용자 프로필 이미지 URL
     */
    private String profileImageUrl;

}
