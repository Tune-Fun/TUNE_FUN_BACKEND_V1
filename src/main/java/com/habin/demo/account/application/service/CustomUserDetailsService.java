package com.habin.demo.account.application.service;

import com.habin.demo.account.application.port.output.LoadAccountPort;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.habin.demo.common.response.MessageCode.ACCOUNT_NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoadAccountPort loadAccountPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadAccountPort.loadCustomUserByUsername(username)
                .orElseThrow(() -> new CommonApplicationException(ACCOUNT_NOT_FOUND));
    }
}
