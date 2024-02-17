package com.tune_fun.v1.account.adapter.output.mail;

import com.tune_fun.v1.external.mail.MailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMailAdapter {

    private final MailHandler mailHandler;

}
