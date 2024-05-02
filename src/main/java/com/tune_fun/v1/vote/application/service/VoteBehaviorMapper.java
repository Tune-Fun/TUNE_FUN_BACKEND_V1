package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperRegisterNotification;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateDeliveryDateNotification;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
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
    @Mapping(target = "offerArtistName", source = "artistName")
    @Mapping(target = "offerGenres", source = "genres")
    @Mapping(target = "offerDurationMs", source = "durationMs")
    @Mapping(target = "offerReleaseDate", source = "releaseDate")
    public abstract SaveVoteChoice saveVoteChoice(final VotePaperCommands.Offer offer);

    @Mapping(target = "title", source = "event", qualifiedByName = "votePaperRegisterFcmTitle")
    @Mapping(target = "body", source = "event.title")
    @Mapping(target = "fcmTokens", source = "devices", qualifiedByName = "fcmTokens")
    public abstract SendVotePaperRegisterNotification sendVotePaperRegisterFcm(final VotePaperRegisterEvent event, final List<NotificationApprovedDevice> devices);

    @Mapping(target = "title", source = "event", qualifiedByName = "votePaperUpdateDeliveryDateFcmTitle")
    @Mapping(target = "body", source = "event.title")
    @Mapping(target = "fcmTokens", source = "devices", qualifiedByName = "fcmTokens")
    public abstract SendVotePaperUpdateDeliveryDateNotification sendVotePaperUpdateDeliveryDateFcm(final VotePaperUpdateDeliveryDateEvent event, final List<NotificationApprovedDevice> devices);

    @Named("votePaperRegisterFcmTitle")
    public String votePaperRegisterFcmTitle(final VotePaperRegisterEvent event) {
        return String.format("[%s]님이 투표를 게재하였습니다.", event.author());
    }

    @Named("votePaperUpdateDeliveryDateFcmTitle")
    public String votePaperUpdateDeliveryDateFcmTitle(final VotePaperUpdateDeliveryDateEvent event) {
        return String.format("[%s]님이 제공일을 설정하였습니다.", event.author());
    }

    @Named("fcmTokens")
    public Set<String> fcmTokens(final List<NotificationApprovedDevice> devices) {
        return devices.stream().map(NotificationApprovedDevice::getFcmToken).collect(toSet());
    }
}
