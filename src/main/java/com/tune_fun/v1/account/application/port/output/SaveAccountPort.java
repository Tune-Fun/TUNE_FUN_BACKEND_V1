package com.tune_fun.v1.account.application.port.output;

import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.value.CurrentAccount;

public interface SaveAccountPort {
    CurrentAccount saveAccount(final SaveAccount behavior);
}
