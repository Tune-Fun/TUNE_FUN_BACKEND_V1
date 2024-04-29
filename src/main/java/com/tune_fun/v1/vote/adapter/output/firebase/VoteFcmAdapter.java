package com.tune_fun.v1.vote.adapter.output.firebase;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.external.firebase.FirebaseMessagingMediator;
import com.tune_fun.v1.external.firebase.FirebaseMto;
import com.tune_fun.v1.vote.application.port.output.SendVoteFcmPort;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperRegisterFcm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VoteFcmAdapter implements SendVoteFcmPort {

    private final FirebaseMessagingMediator firebaseMessagingMediator;

    private final VoteFirebaseMessagingMapper voteFirebaseMessagingMapper;

    @Override
    public void notification(final SendVotePaperRegisterFcm behavior) throws FirebaseMessagingException {
        FirebaseMto.ByTokens mto = voteFirebaseMessagingMapper.fromSendVotePaperRegisterFcmBehavior(behavior);
        firebaseMessagingMediator.sendMulticastMessageByTokens(mto);
    }
}
