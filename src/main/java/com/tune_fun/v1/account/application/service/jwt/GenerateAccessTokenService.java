package com.tune_fun.v1.account.application.service.jwt;

import com.tune_fun.v1.account.application.port.input.usecase.jwt.GenerateAccessTokenUseCase;
import com.tune_fun.v1.account.application.port.output.jwt.CreateAccessTokenPort;
import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@UseCase
@RequiredArgsConstructor
public class GenerateAccessTokenService implements GenerateAccessTokenUseCase {

    private final CreateAccessTokenPort createAccessTokenPort;

    @Override
    public String generateAccessToken(final UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        SaveJwtToken saveJwtToken = new SaveJwtToken(userDetails.getUsername(), authorities);
        return createAccessTokenPort.createAccessToken(saveJwtToken);
    }
}
