package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.interaction.application.port.output.LoadVotePaperVoteCountPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.tune_fun.v1.common.constant.Constants.DOUBLE_COLON;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteCountPersistenceAdapter implements LoadVotePaperVoteCountPort {

    private static final String VOTE_PAPER_VOTE_COUNT_KEY = "vote_paper_vote_count";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Set<String> getVotePaperVoteCountKeys() {
        return redisTemplate.keys(VOTE_PAPER_VOTE_COUNT_KEY + "*");
    }

    @Override
    public String getKey(Long votePaperId) {
        return VOTE_PAPER_VOTE_COUNT_KEY + DOUBLE_COLON + votePaperId;
    }

    @Override
    public Long getVotePaperId(final String key) {
        return Long.parseLong(key.split(DOUBLE_COLON)[1]);
    }

    @Override
    public Map<Object, Object> getVoteData(String votePaperId){
        return redisTemplate.opsForHash().entries(votePaperId);
    }
}
