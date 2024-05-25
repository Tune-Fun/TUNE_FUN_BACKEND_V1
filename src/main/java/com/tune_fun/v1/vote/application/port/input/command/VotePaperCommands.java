package com.tune_fun.v1.vote.application.port.input.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tune_fun.v1.common.validation.Enum;
import com.tune_fun.v1.common.validation.Period;
import com.tune_fun.v1.vote.domain.value.VotePaperOption;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.Set;

public class VotePaperCommands {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Period(start = "voteStartAt", end = "voteEndAt", message = "{vote.paper.vote_period.not_match}")
    public record Register(
            @NotBlank(message = "{vote.paper.title.not_blank}") String title,
            String content,
            @NotNull(message = "{vote.paper.option.not_null}") @Enum(message = "{vote.paper.option.valid}", target = VotePaperOption.class) VotePaperOption option,

            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "{vote.paper.vote_start_at.not_null}")
            @Future(message = "{vote.paper.vote_start_at.future}")
            LocalDateTime voteStartAt,

            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "{vote.paper.vote_end_at.not_null}")
            @Future(message = "{vote.paper.vote_end_at.future}")
            LocalDateTime voteEndAt,

            @Valid
            @NotNull(message = "{vote.paper.offers.not_null}")
            @NotEmpty(message = "{vote.paper.offers.not_empty}")
            @Size(min = 2, message = "{vote.paper.offers.size}")
            Set<Offer> offers
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Offer(
            @NotBlank(message = "{vote.paper.offer.music.not_blank}")
            String id,

            @NotBlank(message = "{vote.paper.offer.music.not_blank}")
            String music,

            String musicImage,

            @NotBlank(message = "{vote.paper.offer.artist_name.not_blank}")
            String artistName
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UpdateDeliveryDate(
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "{vote.paper.delivery_at.not_null}")
            @Future(message = "{vote.paper.delivery_at.future}")
            LocalDateTime deliveryAt
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UpdateVideoUrl(
            @NotBlank(message = "{vote.paper.video_url.not_blank}")
            @URL(message = "{vote.paper.video_url.valid}")
            String videoUrl
    ) {
    }

}
