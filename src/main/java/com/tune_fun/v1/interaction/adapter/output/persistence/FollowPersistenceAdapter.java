package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.common.stereotype.PersistenceAdapter;
import com.tune_fun.v1.interaction.application.port.output.LoadFollowPort;
import com.tune_fun.v1.interaction.application.port.output.SaveFollowPort;
import com.tune_fun.v1.interaction.domain.RegisteredFollow;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements LoadFollowPort, SaveFollowPort {

    private final FollowRepository followRepository;
    private final FollowMapper followMapper;

    @Override
    public Optional<RegisteredFollow> loadFollow(final Long followeeId, final Long followerId) {
        return followRepository.findByFolloweeIdAndFollowerId(followeeId, followerId)
                .map(followMapper::registeredFollow);
    }

    @Override
    public void saveFollow(final Long followeeId, final Long followerId) {
        followRepository.save(followMapper.follow(followeeId, followerId));
    }
}
