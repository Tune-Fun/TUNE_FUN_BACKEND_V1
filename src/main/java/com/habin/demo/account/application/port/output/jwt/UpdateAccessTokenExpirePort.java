package com.habin.demo.account.application.port.output.jwt;

import java.util.Date;

public interface UpdateAccessTokenExpirePort {
    void updateAccessTokenExpire(final String username, final Date expireDate);
}
