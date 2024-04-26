package com.tune_fun.v1.vote.adapter.output.firebase;

import com.tune_fun.v1.external.firebase.FirebaseMessagingMediator;
import com.tune_fun.v1.vote.application.port.output.SendVoteFcmPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VoteFcmAdapter implements SendVoteFcmPort {

    private final FirebaseMessagingMediator firebaseMessagingMediator;

}
