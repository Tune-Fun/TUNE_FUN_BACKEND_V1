package com.tune_fun.v1.interaction.application.port.input.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InteractionCommands {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Follow(@NotNull @Positive Long targetAccountId) {

    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UnFollow(@NotNull @Positive Long targetAccountId) {

    }

}
