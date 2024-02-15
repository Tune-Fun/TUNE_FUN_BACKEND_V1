package com.tune_fun.v1.external.firebase;

import com.google.firebase.messaging.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class FirebaseMessageMapper {

    @Mapping(target = "notification", source = ".", qualifiedByName = "notification")
    @Mapping(target = "androidConfig", source = ".", qualifiedByName = "androidConfig")
    @Mapping(target = "apnsConfig", source = ".", qualifiedByName = "apnsConfig")
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "webpushConfig", ignore = true)
    @Mapping(target = "putAllData", ignore = true)
    @Mapping(target = "fcmOptions", ignore = true)
    @Mapping(target = "condition", ignore = true)
    public abstract Message topicMessage(final FirebaseMto.ByTopic message);

    @Mapping(target = "notification", source = ".", qualifiedByName = "notification")
    @Mapping(target = "androidConfig", source = ".", qualifiedByName = "androidConfig")
    @Mapping(target = "apnsConfig", source = ".", qualifiedByName = "apnsConfig")
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "webpushConfig", ignore = true)
    @Mapping(target = "putAllData", ignore = true)
    @Mapping(target = "fcmOptions", ignore = true)
    @Mapping(target = "condition", ignore = true)
    public abstract Message tokenMessage(final FirebaseMto.ByToken message);

    public MulticastMessage tokensMessage(final FirebaseMto.ByTokens message) {
        MulticastMessage.Builder builder = MulticastMessage.builder();
        builder.setNotification(notification(message));
        builder.setAndroidConfig(androidConfig(message));
        builder.setApnsConfig(apnsConfig(message));
        builder.addAllTokens(message.getTokens());
        return builder.build();
    }

    @Named("notification")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "image", ignore = true)
    public abstract Notification notification(final FirebaseMto.Base message);

    @Named("androidConfig")
    @Mapping(target = "notification", source = ".", qualifiedByName = "androidNotification")
    @Mapping(target = "ttl", ignore = true)
    @Mapping(target = "restrictedPackageName", ignore = true)
    @Mapping(target = "putAllData", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "fcmOptions", ignore = true)
    @Mapping(target = "directBootOk", ignore = true)
    @Mapping(target = "collapseKey", ignore = true)
    public abstract AndroidConfig androidConfig(final FirebaseMto.Base message);

    @Named("androidNotification")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "visibility", ignore = true)
    @Mapping(target = "vibrateTimingsInMillis", ignore = true)
    @Mapping(target = "titleLocalizationKey", ignore = true)
    @Mapping(target = "ticker", ignore = true)
    @Mapping(target = "tag", ignore = true)
    @Mapping(target = "sticky", ignore = true)
    @Mapping(target = "sound", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "notificationCount", ignore = true)
    @Mapping(target = "localOnly", ignore = true)
    @Mapping(target = "lightSettings", ignore = true)
    @Mapping(target = "icon", ignore = true)
    @Mapping(target = "eventTimeInMillis", ignore = true)
    @Mapping(target = "defaultVibrateTimings", ignore = true)
    @Mapping(target = "defaultSound", ignore = true)
    @Mapping(target = "defaultLightSettings", ignore = true)
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "clickAction", ignore = true)
    @Mapping(target = "channelId", ignore = true)
    @Mapping(target = "bodyLocalizationKey", ignore = true)
    public abstract AndroidNotification androidNotification(final FirebaseMto.Base message);

    @Named("apnsConfig")
    @Mapping(target = "aps", source = ".", qualifiedByName = "aps")
    @Mapping(target = "putAllHeaders", ignore = true)
    @Mapping(target = "putAllCustomData", ignore = true)
    @Mapping(target = "fcmOptions", ignore = true)
    public abstract ApnsConfig apnsConfig(final FirebaseMto.Base message);

    @Named("aps")
    @Mapping(target = "alert", source = ".", qualifiedByName = "apsAlert")
    @Mapping(target = "sound", expression = "java(\"default\")")
    @Mapping(target = "contentAvailable", constant = "true")
    @Mapping(target = "mutableContent", constant = "false")
    @Mapping(target = "badge", ignore = true)
    @Mapping(target = "threadId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "putAllCustomData", ignore = true)
    public abstract Aps aps(final FirebaseMto.Base message);

    @Named("apsAlert")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "launchImage", ignore = true)
    @Mapping(target = "subtitle", ignore = true)
    @Mapping(target = "titleLocalizationKey", ignore = true)
    @Mapping(target = "subtitleLocalizationKey", ignore = true)
    @Mapping(target = "localizationKey", ignore = true)
    @Mapping(target = "actionLocalizationKey", ignore = true)
    public abstract ApsAlert apsAlert(final FirebaseMto.Base message);

}
