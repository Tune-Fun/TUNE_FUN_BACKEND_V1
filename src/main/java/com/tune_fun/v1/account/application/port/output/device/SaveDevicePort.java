package com.tune_fun.v1.account.application.port.output.device;

import com.tune_fun.v1.account.domain.behavior.SaveDevice;

public interface SaveDevicePort {
    void saveDevice(final SaveDevice behavior);
}
