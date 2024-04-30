package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public interface UpdateDeliveryAtPort {

    RegisteredVotePaper updateDeliveryAt(@NotNull final Long votePaperId, @NotNull final LocalDateTime deliveryAt);

}
