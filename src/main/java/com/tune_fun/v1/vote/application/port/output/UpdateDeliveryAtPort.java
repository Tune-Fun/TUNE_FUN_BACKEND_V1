package com.tune_fun.v1.vote.application.port.output;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public interface UpdateDeliveryAtPort {

    void updateDeliveryAt(@NotNull final Long votePaperId, @NotNull final LocalDateTime deliveryAt);

}
