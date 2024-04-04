package com.tune_fun.v1.account.application.port.output.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface DeleteAuthorizationRequestPort {
    void delete(HttpServletRequest request, HttpServletResponse response);
}
