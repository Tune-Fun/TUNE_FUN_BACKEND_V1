package com.tune_fun.v1.vote.adapter.output.firebase;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.external.firebase.FirebaseMessagingMediator;
import com.tune_fun.v1.vote.application.port.output.SendVoteUploadFcmPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@XRayEnabled
@Component
@RequiredArgsConstructor
public class VoteFcmAdapter implements SendVoteUploadFcmPort {

    private final FirebaseMessagingMediator firebaseMessagingMediator;

}
