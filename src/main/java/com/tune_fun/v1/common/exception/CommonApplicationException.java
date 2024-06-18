package com.tune_fun.v1.common.exception;

import com.tune_fun.v1.common.response.MessageCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CommonApplicationException extends RuntimeException implements Supplier<CommonApplicationException> {

    private MessageCode messageCode;

    @Override
    public CommonApplicationException get() {
        return this;
    }
}
