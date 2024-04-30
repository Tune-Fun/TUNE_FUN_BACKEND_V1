package com.tune_fun.v1.vote.application.port.input.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tune_fun.v1.common.validation.Period;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class VotePaperCommands {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Period(start = "voteStartAt", end = "voteEndAt", message = "{vote.paper.vote_period.not_match}")
    public record Register(
            @NotBlank(message = "{vote.paper.title.not_blank}") String title,
            @NotBlank(message = "{vote.paper.content.not_blank}") String content,
            @NotBlank(message = "{vote.paper.option.not_blank}") String option,

            @NotNull(message = "{vote.paper.vote_start_at.not_null}")
            @Future(message = "{vote.paper.vote_start_at.future}")
            LocalDateTime voteStartAt,

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
            String music,

            @NotBlank(message = "{vote.paper.offer.artist_name.not_blank}")
            String artistName,

            @NotNull(message = "{vote.paper.offer.genres.not_null}")
            @NotEmpty(message = "{vote.paper.offer.genres.not_empty}")
            List<String> genres,

            @NotNull(message = "{vote.paper.offer.duration_ms.not_null}")
            @Positive(message = "{vote.paper.offer.duration_ms.positive}")
            Integer durationMs,

            @NotBlank(message = "{vote.paper.offer.release_date.not_blank}")
            String releaseDate
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SetDeliveryDate(
            Long votePaperId,

            LocalDateTime deliveryAt
    ) {
    }
}
