package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.vote.domain.RegisteredVotePaper;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        config = BaseMapperConfig.class,
        imports = StringUtil.class
)
public abstract class VotePaperMapper {

    @Mapping(target = "option", source = "option", qualifiedByName = "toValue")
    public abstract RegisteredVotePaper registeredVotePaper(final VotePaperMongoEntity votePaperMongoEntity);

    @Named("toValue")
    public String toValue(final VotePaperOption option) {
        return option.getValue();
    }

    @Mapping(target = "uuid", expression = "java(StringUtil.uuid())")
    @Mapping(target = "option", source = "option", qualifiedByName = "votePaperOption")
    public abstract VotePaperMongoEntity fromSaveVotePaperBehavior(final SaveVotePaper saveVotePaper);

    @Named("votePaperOption")
    public VotePaperOption votePaperOption(final String option) {
        return VotePaperOption.fromValue(option);
    }
}
