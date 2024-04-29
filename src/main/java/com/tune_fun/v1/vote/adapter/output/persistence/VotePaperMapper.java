package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
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
    public abstract RegisteredVotePaper registeredVotePaper(final VotePaperJpaEntity votePaperJpaEntity);

    @Named("toValue")
    public String toValue(final VotePaperOption option) {
        return option.getValue();
    }

    @Mapping(target = "uuid", expression = "java(StringUtil.uuid())")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "option", source = "saveVotePaper.option", qualifiedByName = "votePaperOption")
    public abstract VotePaperJpaEntity fromSaveVotePaperBehavior(final SaveVotePaper saveVotePaper, final AccountJpaEntity author);

    @Named("votePaperOption")
    public VotePaperOption votePaperOption(final String option) {
        return VotePaperOption.fromValue(option);
    }

    @Mapping(target = "votePaper", source = "votePaperJpaEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract VoteChoiceJpaEntity.VoteChoiceJpaEntityBuilder<?, ?> updateVotePaper(VotePaperJpaEntity votePaperJpaEntity,
                                                                                         @MappingTarget final VoteChoiceJpaEntity.VoteChoiceJpaEntityBuilder<?, ?> voteChoiceJpaEntityBuilder);

    @IterableMapping(qualifiedByName = "fromSaveVoteChoiceBehavior")
    public abstract Set<VoteChoiceJpaEntity> fromSaveVoteChoiceBehaviors(final Set<SaveVoteChoice> behavior);

    @Named("fromSaveVoteChoiceBehavior")
    @Mapping(target = "uuid", expression = "java(StringUtil.uuid())")
    @Mapping(target = "offer", source = ".", qualifiedByName = "voteChoiceOffer")
    public abstract VoteChoiceJpaEntity fromSaveVoteChoiceBehavior(final SaveVoteChoice behavior);

    @Named("voteChoiceOffer")
    @Mapping(target = "music", source = "offerName")
    @Mapping(target = "artistName", source = "offerArtistName")
    @Mapping(target = "genres", source = "offerGenres")
    @Mapping(target = "releaseDate", source = "offerReleaseDate")
    @Mapping(target = "durationMs", source = "offerDurationMs")
    public abstract Offer voteChoiceOffer(final SaveVoteChoice behavior);
}
