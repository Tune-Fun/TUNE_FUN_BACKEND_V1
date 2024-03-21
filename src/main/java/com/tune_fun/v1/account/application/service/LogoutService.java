package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.LogoutUseCase;
import com.tune_fun.v1.account.application.port.output.device.DeleteDevicePort;
import com.tune_fun.v1.account.application.port.output.jwt.DeleteAccessTokenPort;
import com.tune_fun.v1.account.application.port.output.jwt.DeleteRefreshTokenPort;
import com.tune_fun.v1.account.domain.behavior.DeleteDevice;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;


@Service
@UseCase
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    private final DeleteAccessTokenPort deleteAccessTokenPort;
    private final DeleteRefreshTokenPort deleteRefreshTokenPort;
    private final DeleteDevicePort deleteDevicePort;

    @Override
    public void logout(final String accessToken, final AccountCommands.Device device, final User user) {
        deleteAccessTokenPort.deleteAccessToken(accessToken);
        deleteRefreshTokenPort.deleteRefreshToken(accessToken);

        DeleteDevice deleteDeviceBehavior = new DeleteDevice(user.getUsername(), device.fcmToken(), device.deviceToken());
        deleteDevicePort.delete(deleteDeviceBehavior);
    }
}
