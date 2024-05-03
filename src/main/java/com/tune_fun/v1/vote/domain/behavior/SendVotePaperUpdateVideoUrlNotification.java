package com.tune_fun.v1.vote.domain.behavior;

import java.util.Set;

public record SendVotePaperUpdateVideoUrlNotification(String title, String body, Set<String> fcmTokens,
                                                      String videoUrl) {
}
