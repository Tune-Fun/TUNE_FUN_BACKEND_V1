package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.domain.behavior.*;
import com.tune_fun.v1.vote.domain.event.VotePaperDeadlineEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateVideoUrlEvent;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Mapper(config = BaseMapperConfig.class)
public abstract class VoteBehaviorMapper {

    @Mapping(target = "author", source = "user.username")
    public abstract SaveVotePaper saveVotePaper(final VotePaperCommands.Register command, final User user);

    @IterableMapping(qualifiedByName = "saveVoteChoice")
    public abstract Set<SaveVoteChoice> saveVoteChoices(final Set<VotePaperCommands.Offer> offers);

    @Named("saveVoteChoice")
    @Mapping(target = "artistName", source = "artistName")
    public abstract SaveVoteChoice saveVoteChoice(final VotePaperCommands.Offer offer);

    @Mapping(target = "title", source = "event", qualifiedByName = "votePaperRegisterNotificationTitle")
    @Mapping(target = "body", source = "event.title")
    @Mapping(target = "fcmTokens", source = "devices", qualifiedByName = "fcmTokens")
    public abstract SendVotePaperRegisterNotification sendVotePaperRegisterNotification(final VotePaperRegisterEvent event, final List<NotificationApprovedDevice> devices);

    @Mapping(target = "title", source = "event", qualifiedByName = "votePaperEndNotificationTitle")
    @Mapping(target = "body", source = "event.title")
    @Mapping(target = "fcmTokens", source = "devices", qualifiedByName = "fcmTokens")
    public abstract SendVotePaperEndNotification sendVotePaperEndNotification(final VotePaperDeadlineEvent event, final List<NotificationApprovedDevice> devices);

    @Mapping(target = "title", source = "event", qualifiedByName = "votePaperUpdateDeliveryDateNotificationTitle")
    @Mapping(target = "body", source = "event.title")
    @Mapping(target = "fcmTokens", source = "devices", qualifiedByName = "fcmTokens")
    public abstract SendVotePaperUpdateDeliveryDateNotification sendVotePaperUpdateDeliveryDateNotification(final VotePaperUpdateDeliveryDateEvent event, final List<NotificationApprovedDevice> devices);

    @Mapping(target = "title", source = "event", qualifiedByName = "votePaperUpdateVideoUrlNotificationTitle")
    @Mapping(target = "body", source = "event.title")
    @Mapping(target = "fcmTokens", source = "devices", qualifiedByName = "fcmTokens")
    @Mapping(target = "videoUrl", source = "event.videoUrl")
    public abstract SendVotePaperUpdateVideoUrlNotification sendVotePaperUpdateVideoUrlNotification(final VotePaperUpdateVideoUrlEvent event, final List<NotificationApprovedDevice> devices);

    @Named("votePaperRegisterNotificationTitle")
    public String votePaperRegisterNotificationTitle(final VotePaperRegisterEvent event) {
        return String.format("[%s]님이 투표를 게재하였습니다.", event.author());
    }

    @Named("votePaperEndNotificationTitle")
    public String votePaperEndNotificationTitle(final VotePaperDeadlineEvent event) {
        return String.format("[%s]님의 투표가 종료되었습니다. 최종 선정된 곡을 확인해 주세요.", event.author());
    }

    @Named("votePaperUpdateDeliveryDateNotificationTitle")
    public String votePaperUpdateDeliveryDateNotificationTitle(final VotePaperUpdateDeliveryDateEvent event) {
        return String.format("[%s]님이 제공일을 설정하였습니다.", event.author());
    }

    @Named("votePaperUpdateVideoUrlNotificationTitle")
    public String votePaperUpdateVideoUrlNotificationTitle(final VotePaperUpdateVideoUrlEvent event) {
        return String.format("[%s]님이 선정된 곡에 대해 영상을 업로드 하였습니다.", event.author());
    }

    @Named("fcmTokens")
    public Set<String> fcmTokens(final List<NotificationApprovedDevice> devices) {
        return devices.stream().map(NotificationApprovedDevice::getFcmToken).collect(toSet());
    }
}
