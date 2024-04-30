package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.vote.domain.value.RegisteredVote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class)
public abstract class VoteMapper {

    @Mapping(target = "username", source = "voter.username")
    @Mapping(target = "votePaperId", source = "voteChoice.votePaper.id")
    @Mapping(target = "music", source = "voteChoice.offer.music")
    @Mapping(target = "artistName", source = "voteChoice.offer.artistName")
    public abstract RegisteredVote registeredVote(final VoteJpaEntity voteJpaEntity);

}
