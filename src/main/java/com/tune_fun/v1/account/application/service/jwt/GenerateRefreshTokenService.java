package com.tune_fun.v1.account.application.service.jwt;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.GenerateRefreshTokenUseCase;
import com.tune_fun.v1.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@XRayEnabled
@Service
@UseCase
@RequiredArgsConstructor
public class GenerateRefreshTokenService implements GenerateRefreshTokenUseCase {

    private final CreateRefreshTokenPort createRefreshTokenPort;

    @Override
    public String generateRefreshToken(final UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        SaveJwtToken saveJwtToken = new SaveJwtToken(userDetails.getUsername(), authorities);
        return createRefreshTokenPort.createRefreshToken(saveJwtToken);
    }
}
