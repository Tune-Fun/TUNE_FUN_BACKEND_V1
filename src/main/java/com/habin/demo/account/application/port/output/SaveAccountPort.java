package com.habin.demo.account.application.port.output;

import com.habin.demo.account.domain.state.CurrentAccount;
import com.habin.demo.account.domain.behavior.SaveAccount;

public interface SaveAccountPort {
    CurrentAccount saveAccount(final SaveAccount saveAccount);
}
