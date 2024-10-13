package com.tune_fun.v1.interaction.application.port.output;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface LoadVotePaperVoteCountPort {

    Set<String> getVotePaperVoteCountKeys();

    String getKey(Long votePaperId);

    Long getVotePaperId(String key);

    Map<Object, Object> getVoteData(String votePaperId);

}
