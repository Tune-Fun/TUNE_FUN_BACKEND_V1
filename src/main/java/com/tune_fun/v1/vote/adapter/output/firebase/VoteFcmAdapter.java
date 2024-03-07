package com.tune_fun.v1.vote.adapter.output.firebase;

import com.tune_fun.v1.external.firebase.FirebaseMessagingMediator;
import com.tune_fun.v1.vote.application.port.output.SendVoteUploadFcmPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VoteFcmAdapter implements SendVoteUploadFcmPort {

    private final FirebaseMessagingMediator firebaseMessagingMediator;

}
