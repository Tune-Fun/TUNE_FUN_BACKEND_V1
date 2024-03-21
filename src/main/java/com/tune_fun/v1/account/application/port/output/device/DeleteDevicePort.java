package com.tune_fun.v1.account.application.port.output.device;

import com.tune_fun.v1.account.domain.behavior.DeleteDevice;

public interface DeleteDevicePort {
    void delete(final DeleteDevice behavior);
}
