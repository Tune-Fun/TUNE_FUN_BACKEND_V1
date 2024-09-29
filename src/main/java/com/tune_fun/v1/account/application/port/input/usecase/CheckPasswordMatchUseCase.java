package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.account.application.port.input.query.AccountQueries;
import com.tune_fun.v1.account.domain.value.CheckPasswordMathResult;

public interface CheckPasswordMatchUseCase {

    CheckPasswordMathResult checkPasswordMatch(AccountQueries.Password query, String username);
}
