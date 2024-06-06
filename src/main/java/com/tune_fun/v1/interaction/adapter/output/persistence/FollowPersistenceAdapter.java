package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.common.stereotype.PersistenceAdapter;
import com.tune_fun.v1.interaction.application.port.output.LoadFollowPort;
import com.tune_fun.v1.interaction.application.port.output.SaveFollowPort;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements LoadFollowPort, SaveFollowPort {

    private final FollowRepository followRepository;


}
