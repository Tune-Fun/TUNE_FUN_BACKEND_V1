package com.habin.demo.account.application.port.output.jwt;

import java.util.Date;

public interface UpdateRefreshTokenExpirePort {
    void updateRefreshTokenExpire(final String username, final Date expireDate);
}
