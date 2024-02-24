package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.account.application.port.input.query.AccountQueries;

@FunctionalInterface
public interface FindUsernameUseCase {
    void findUsername(final AccountQueries.Username query) throws Exception;
}
