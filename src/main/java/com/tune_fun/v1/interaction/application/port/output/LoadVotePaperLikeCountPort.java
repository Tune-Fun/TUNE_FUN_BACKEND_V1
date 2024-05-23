package com.tune_fun.v1.interaction.application.port.output;

import java.util.Set;

public interface LoadVotePaperLikeCountPort {

    Set<String> getVotePaperLikeCountKeys();

    Long getVotePaperLikeCount(final String key);

    String getKey(Long votePaperId);

    Long getVotePaperId(String key);
}
