package com.tune_fun.v1.vote.application.port.output;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperEndNotification;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperRegisterNotification;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateDeliveryDateNotification;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateVideoUrlNotification;

public interface SendVoteNotificationPort {
    void notification(final SendVotePaperRegisterNotification behavior) throws FirebaseMessagingException;

    void notification(final SendVotePaperEndNotification behavior) throws FirebaseMessagingException;

    void notification(final SendVotePaperUpdateDeliveryDateNotification behavior) throws FirebaseMessagingException;

    void notification(final SendVotePaperUpdateVideoUrlNotification behavior) throws FirebaseMessagingException;
}
