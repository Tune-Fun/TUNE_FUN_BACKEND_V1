package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import org.mapstruct.*;

import java.util.Set;

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

    @Mapping(target = "id", expression = "java(StringUtil.uuid())")
    @Mapping(target = "option", source = "option", qualifiedByName = "votePaperOption")
    public abstract VotePaperMongoEntity fromSaveVotePaperBehavior(final SaveVotePaper saveVotePaper);

    @Named("votePaperOption")
    public VotePaperOption votePaperOption(final String option) {
        return VotePaperOption.fromValue(option);
    }

    @Mapping(target = "votePaper", source = "votePaperMongoEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract VoteChoiceMongoEntity.VoteChoiceMongoEntityBuilder updateVotePaper(VotePaperMongoEntity votePaperMongoEntity,
                                                                                       @MappingTarget final VoteChoiceMongoEntity.VoteChoiceMongoEntityBuilder voteChoiceMongoEntity);

    @IterableMapping(qualifiedByName = "fromSaveVoteChoiceBehavior")
    public abstract Set<VoteChoiceMongoEntity> fromSaveVoteChoiceBehaviors(final Set<SaveVoteChoice> behavior);

    @Named("fromSaveVoteChoiceBehavior")
    @Mapping(target = "id", expression = "java(StringUtil.uuid())")
    @Mapping(target = "offer", source = ".", qualifiedByName = "voteChoiceOffer")
    public abstract VoteChoiceMongoEntity fromSaveVoteChoiceBehavior(final SaveVoteChoice behavior);

    @Named("voteChoiceOffer")
    @Mapping(target = "name", source = "offerName")
    @Mapping(target = "artistName", source = "offerArtistName")
    @Mapping(target = "genres", source = "offerGenres")
    @Mapping(target = "releaseDate", source = "offerReleaseDate")
    @Mapping(target = "durationMs", source = "offerDurationMs")
    public abstract Offer voteChoiceOffer(final SaveVoteChoice behavior);
}
