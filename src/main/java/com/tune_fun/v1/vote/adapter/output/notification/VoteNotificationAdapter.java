package com.tune_fun.v1.vote.adapter.output.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.external.firebase.FirebaseMessagingMediator;
import com.tune_fun.v1.external.firebase.FirebaseMto;
import com.tune_fun.v1.vote.application.port.output.SendVoteNotificationPort;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperRegisterNotification;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateDeliveryDateNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VoteNotificationAdapter implements SendVoteNotificationPort {

    private final FirebaseMessagingMediator firebaseMessagingMediator;

    private final VoteFirebaseMessagingMapper voteFirebaseMessagingMapper;

    @Override
    public void notification(final SendVotePaperRegisterNotification behavior) throws FirebaseMessagingException {
        FirebaseMto.ByTokens mto = voteFirebaseMessagingMapper.fromSendVotePaperRegisterNotificationBehavior(behavior);
        firebaseMessagingMediator.sendMulticastMessageByTokens(mto);
    }

    @Override
    public void notification(SendVotePaperUpdateDeliveryDateNotification behavior) throws FirebaseMessagingException {
        FirebaseMto.ByTokens mto = voteFirebaseMessagingMapper.fromSendVotePaperUpdateDeliveryDateNotificationBehavior(behavior);
        firebaseMessagingMediator.sendMulticastMessageByTokens(mto);
    }
}
