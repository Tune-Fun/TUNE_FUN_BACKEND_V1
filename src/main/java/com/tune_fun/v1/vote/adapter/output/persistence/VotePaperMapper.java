package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.vote.domain.RegisteredVotePaper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = BaseMapperConfig.class)
public abstract class VotePaperMapper {

    @Mapping(target = "option", source = "option", qualifiedByName = "toValue")
    public abstract RegisteredVotePaper registeredVotePaper(VotePaperMongoEntity votePaperMongoEntity);

    @Named("toValue")
    public String toValue(final VotePaperOption option) {
        return option.getValue();
    }

}
