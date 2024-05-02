package com.tune_fun.v1.vote.domain.behavior;

import java.util.Map;
import java.util.Set;

public record SendVotePaperUpdateDeliveryDateNotification(
        String title,
        String body,
        Map<String, String> data,
        Set<String> fcmTokens
) {
}
