package com.tune_fun.v1.interaction.domain;

import com.tune_fun.v1.common.response.BasePayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public record ArtistInfo (Long id, String nickname, String profileImageUrl) implements BasePayload {
}
