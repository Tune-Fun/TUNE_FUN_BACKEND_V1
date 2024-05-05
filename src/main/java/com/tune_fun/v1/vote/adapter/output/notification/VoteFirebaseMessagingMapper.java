package com.tune_fun.v1.vote.adapter.output.notification;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.external.firebase.FirebaseMto;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperRegisterNotification;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateDeliveryDateNotification;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateVideoUrlNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(config = BaseMapperConfig.class)
public abstract class VoteFirebaseMessagingMapper {

    @Mapping(target = "tokens", source = "fcmTokens")
    public abstract FirebaseMto.ByTokens fromSendVotePaperRegisterNotificationBehavior(final SendVotePaperRegisterNotification behavior);

    @Mapping(target = "tokens", source = "fcmTokens")
    public abstract FirebaseMto.ByTokens fromSendVotePaperUpdateDeliveryDateNotificationBehavior(final SendVotePaperUpdateDeliveryDateNotification behavior);

    @Mapping(target = "tokens", source = "fcmTokens")
    @Mapping(target = "data", source = ".", qualifiedByName = "getData")
    public abstract FirebaseMto.ByTokens fromSendVotePaperUpdateVideoUrlNotificationBehavior(final SendVotePaperUpdateVideoUrlNotification behavior);

    @Named("getData")
    public Map<String, String> getData(final SendVotePaperUpdateVideoUrlNotification behavior) {
        return Map.of("videoUrl", behavior.videoUrl());
    }
}