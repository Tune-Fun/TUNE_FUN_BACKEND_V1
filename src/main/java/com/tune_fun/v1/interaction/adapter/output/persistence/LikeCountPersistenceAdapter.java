package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.interaction.application.port.output.LoadVotePaperLikeCountPort;
import com.tune_fun.v1.interaction.application.port.output.SaveVotePaperLikeCountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class LikeCountPersistenceAdapter implements SaveVotePaperLikeCountPort, LoadVotePaperLikeCountPort {

    private static final String VOTE_PAPER_LIKE_COUNT_KEY = "vote_paper_like_count";

    private static final String LIKE_COUNT_HASH_KEY = "like_count";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Set<Long> getVotePaperIds() {
        Set<String> keys = redisTemplate.keys(VOTE_PAPER_LIKE_COUNT_KEY + "*");

        if (keys == null) return Set.of();

        return keys.stream().map(key -> Long.valueOf(key.split("::")[1])).collect(toSet());
    }

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
