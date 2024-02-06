package com.habin.demo.account.application.service.jwt;

import com.habin.demo.account.application.port.input.usecase.jwt.GenerateRefreshTokenUseCase;
import com.habin.demo.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.habin.demo.account.domain.behavior.SaveJwtToken;
import com.habin.demo.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
