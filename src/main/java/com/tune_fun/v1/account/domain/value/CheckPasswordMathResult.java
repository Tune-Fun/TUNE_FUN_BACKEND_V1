package com.tune_fun.v1.account.domain.value;

import com.tune_fun.v1.common.response.BasePayload;

public record CheckPasswordMathResult(
        boolean matched
) implements BasePayload {

}
