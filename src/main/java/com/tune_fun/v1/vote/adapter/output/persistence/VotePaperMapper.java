package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(
        config = BaseMapperConfig.class,
        imports = StringUtil.class
)
public abstract class VotePaperMapper {

    @Mapping(target = "option", source = "option", qualifiedByName = "toValue")
    @Mapping(target = "author", source = "author.nickname")
    public abstract RegisteredVotePaper registeredVotePaper(final VotePaperJpaEntity votePaperJpaEntity);

    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(target = "authorNickname", source = "author.nickname")
    @Mapping(target = "totalVoteCount", constant = "0")
    @Mapping(target = "totalFavCount", constant = "0")
    public abstract ScrollableVotePaper scrollableVotePaper(final VotePaperJpaEntity votePaperJpaEntity);

    @Named("toValue")
    public String toValue(final VotePaperOption option) {
        return option.getValue();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "uuid", expression = "java(StringUtil.uuid())")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "option", source = "saveVotePaper.option", qualifiedByName = "votePaperOption")
    public abstract VotePaperJpaEntity fromSaveVotePaperBehavior(final SaveVotePaper saveVotePaper, final AccountJpaEntity author);

    @Named("votePaperOption")
    public VotePaperOption votePaperOption(final String option) {
        return VotePaperOption.fromValue(option);
    }

    @Mapping(target = "deliveryAt", source = "deliveryAt")
    public abstract VotePaperJpaEntity.VotePaperJpaEntityBuilder<?, ?> updateDeliveryAt(LocalDateTime deliveryAt, @MappingTarget VotePaperJpaEntity.VotePaperJpaEntityBuilder<?, ?> builder);

}
