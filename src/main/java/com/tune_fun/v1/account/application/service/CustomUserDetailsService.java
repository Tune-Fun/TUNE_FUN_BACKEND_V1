package com.tune_fun.v1.account.application.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.common.response.MessageCode.ACCOUNT_NOT_FOUND;

@XRayEnabled
@Service
@UseCase
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoadAccountPort loadAccountPort;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadAccountPort.loadCustomUserByUsername(username)
                .orElseThrow(() -> new CommonApplicationException(ACCOUNT_NOT_FOUND));
    }
}
