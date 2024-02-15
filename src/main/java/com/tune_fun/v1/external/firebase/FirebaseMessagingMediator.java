package com.tune_fun.v1.external.firebase;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.google.firebase.messaging.FirebaseMessaging.getInstance;

@Component
@RequiredArgsConstructor
public class FirebaseMessagingMediator {

    private final FirebaseMessageMapper firebaseMessageMapper;

    public void sendMessageByTopic(final FirebaseMto.ByTopic message) throws FirebaseMessagingException {
        getInstance().send(firebaseMessageMapper.topicMessage(message));
    }

    public void sendMessageByToken(final FirebaseMto.ByToken message) throws FirebaseMessagingException {
        getInstance().send(firebaseMessageMapper.tokenMessage(message));
    }

    public void sendMulticastMessageByTokens(final FirebaseMto.ByTokens message) throws FirebaseMessagingException {
        getInstance().sendEachForMulticast(firebaseMessageMapper.tokensMessage(message));
    }

}
