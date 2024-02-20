package com.tune_fun.v1.account.adapter.output.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationConfig {

    @Builder.Default
    @Column(name = "vote_progress_notification", nullable = false)
    private Boolean voteProgressNotification = true;

    @Builder.Default
    @Column(name = "vote_end_notification", nullable = false)
    private Boolean voteEndNotification = true;

    @Builder.Default
    @Column(name = "vote_delivery_notification", nullable = false)
    private Boolean voteDeliveryNotification = true;

}
