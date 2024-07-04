package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.common.stereotype.PersistenceAdapter;
import com.tune_fun.v1.interaction.application.port.output.DeleteFollowPort;
import com.tune_fun.v1.interaction.application.port.output.LoadFollowPort;
import com.tune_fun.v1.interaction.application.port.output.SaveFollowPort;
import com.tune_fun.v1.interaction.domain.RegisteredFollow;
import com.tune_fun.v1.interaction.domain.ScrollableFollowInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Window;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements LoadFollowPort, SaveFollowPort, DeleteFollowPort {

    private final FollowRepository followRepository;
    private final FollowMapper followMapper;

    @Override
    public Set<Long> loadFollowerIds(final Long followeeId) {
        return followRepository.findByFolloweeId(followeeId)
                .stream()
                .map(FollowJpaEntity::getFollowerId)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<RegisteredFollow> loadFollow(final Long followeeId, final Long followerId) {
        return followRepository.findByFolloweeIdAndFollowerId(followeeId, followerId)
                .map(followMapper::registeredFollow);
    }

    @Override
    public Window<ScrollableFollowInfo> scrollFollow(Long lastId, Long followerId) {
        return null;
    }

    @Override
    public void saveFollow(final Long followeeId, final Long followerId) {
        followRepository.save(followMapper.follow(followeeId, followerId));
    }

    @Override
    public void deleteFollow(final Long followeeId, final Long followerId) {
        followRepository.deleteByFolloweeIdAndFollowerId(followeeId, followerId);
    }
}
