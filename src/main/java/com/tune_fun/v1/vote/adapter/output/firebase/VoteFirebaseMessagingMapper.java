package com.tune_fun.v1.vote.adapter.output.firebase;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.external.firebase.FirebaseMto;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperRegisterFcm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class)
public abstract class VoteFirebaseMessagingMapper {

    @Mapping(target = "tokens", source = "fcmTokens")
    public abstract FirebaseMto.ByTokens fromSendVotePaperRegisterFcmBehavior(final SendVotePaperRegisterFcm behavior);

}
