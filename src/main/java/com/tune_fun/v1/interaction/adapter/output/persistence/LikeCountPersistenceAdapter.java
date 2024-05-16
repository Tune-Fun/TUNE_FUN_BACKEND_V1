package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.interaction.application.port.output.SaveVotePaperLikeCountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeCountPersistenceAdapter implements SaveVotePaperLikeCountPort {

    private static final String VOTE_PAPER_LIKE_COUNT_KEY = "vote_paper_like_count";

    private static final String LIKE_COUNT_HASH_KEY = "like_count";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void incrementVotePaperLikeCount(final Long votePaperId) {
        redisTemplate.opsForHash().increment(key(votePaperId), LIKE_COUNT_HASH_KEY, 1);
    }

    @Override
    public void decrementVotePaperLikeCount(final Long votePaperId) {
        redisTemplate.opsForHash().increment(key(votePaperId), LIKE_COUNT_HASH_KEY, -1);
    }

    private static String key(final Long votePaperId) {
        return VOTE_PAPER_LIKE_COUNT_KEY + "::" + votePaperId;
    }

}
