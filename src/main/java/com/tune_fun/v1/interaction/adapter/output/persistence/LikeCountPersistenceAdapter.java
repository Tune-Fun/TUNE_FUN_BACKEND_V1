package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.interaction.application.port.output.LoadVotePaperLikeCountPort;
import com.tune_fun.v1.interaction.application.port.output.SaveVotePaperLikeCountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikeCountPersistenceAdapter implements SaveVotePaperLikeCountPort, LoadVotePaperLikeCountPort {

    private static final String VOTE_PAPER_LIKE_COUNT_KEY = "vote_paper_like_count";

    private static final String LIKE_COUNT_HASH_KEY = "like_count";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Set<String> getVotePaperLikeCountKeys() {
        return redisTemplate.keys(VOTE_PAPER_LIKE_COUNT_KEY + "*");
    }

    @Override
    public Long getVotePaperLikeCount(String key) {
        return (Long) redisTemplate.opsForHash().get(key, LIKE_COUNT_HASH_KEY);
    }

    @Override
    public void incrementVotePaperLikeCount(final Long votePaperId) {
        redisTemplate.opsForHash().increment(getKey(votePaperId), LIKE_COUNT_HASH_KEY, 1);
    }

    @Override
    public void decrementVotePaperLikeCount(final Long votePaperId) {
        redisTemplate.opsForHash().increment(getKey(votePaperId), LIKE_COUNT_HASH_KEY, -1);
    }

    @Override
    public String getKey(final Long votePaperId) {
        return VOTE_PAPER_LIKE_COUNT_KEY + "::" + votePaperId;
    }

    @Override
    public Long getVotePaperId(final String key) {
        return Long.parseLong(key.split("::")[1]);
    }

}
