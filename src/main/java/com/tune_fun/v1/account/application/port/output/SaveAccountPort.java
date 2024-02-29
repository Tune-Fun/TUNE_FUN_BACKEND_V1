package com.tune_fun.v1.account.application.port.output;

import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.state.CurrentAccount;

public interface SaveAccountPort {
    CurrentAccount saveAccount(final SaveAccount saveAccount);
}
