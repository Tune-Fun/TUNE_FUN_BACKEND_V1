package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.interaction.application.port.output.LoadVotePaperLikeCountPort;
import com.tune_fun.v1.interaction.application.port.output.SaveVotePaperLikeCountPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

import static com.tune_fun.v1.common.constant.Constants.DOUBLE_COLON;

@Slf4j
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
        log.info("Get vote paper like count for key: {}", key);
        String value = String.valueOf(redisTemplate.opsForValue().get(key));
        return Long.parseLong(value);
    }

    @Override
    public void incrementVotePaperLikeCount(final Long votePaperId) {
        getVotePaperLikeCountById(votePaperId).ifPresentOrElse(
                likeCount -> redisTemplate.opsForValue().increment(getKey(votePaperId)),
                () -> redisTemplate.opsForValue().set(getKey(votePaperId), 1)
        );
    }

    @Override
    public void decrementVotePaperLikeCount(final Long votePaperId) {
        Optional<Object> votePaperLikeCountById = getVotePaperLikeCountById(votePaperId);

        if (votePaperLikeCountById.isEmpty()) {
            redisTemplate.opsForValue().set(getKey(votePaperId), 0);
            return;
        }

        log.info("vote paper like count : {}", votePaperLikeCountById.get());

        if (Long.parseLong(String.valueOf(votePaperLikeCountById.get())) == 0)
            return;

        redisTemplate.opsForValue().decrement(getKey(votePaperId));
    }

    @Override
    public String getKey(final Long votePaperId) {
        return VOTE_PAPER_LIKE_COUNT_KEY + DOUBLE_COLON + votePaperId;
    }

    @Override
    public Long getVotePaperId(final String key) {
        return Long.parseLong(key.split(DOUBLE_COLON)[1]);
    }

    @Override
    public Optional<Object> getVotePaperLikeCountById(final Long votePaperId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(votePaperId)));
    }

}
