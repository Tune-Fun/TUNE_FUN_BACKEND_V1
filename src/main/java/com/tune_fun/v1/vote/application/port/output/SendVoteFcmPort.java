package com.tune_fun.v1.vote.application.port.output;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperRegisterFcm;

public interface SendVoteFcmPort {
    void notification(final SendVotePaperRegisterFcm sendVotePaperRegisterFcmBehavior) throws FirebaseMessagingException;
}
