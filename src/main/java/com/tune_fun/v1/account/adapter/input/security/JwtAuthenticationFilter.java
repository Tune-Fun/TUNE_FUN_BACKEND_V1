package com.tune_fun.v1.account.adapter.input.security;

import com.tune_fun.v1.account.application.port.input.usecase.jwt.LoadUsernameFromTokenUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.response.ExceptionResponse;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.util.ObjectUtil;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.common.util.i18n.MessageSourceUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final ValidateAccessTokenUseCase validateAccessTokenUseCase;
    private final LoadUsernameFromTokenUseCase loadUsernameFromTokenUseCase;

    private final ObjectUtil objectUtil;
    private final MessageSourceUtil messageSourceUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = getAccessTokenFromRequest(request);

            if (validateAccessTokenUseCase.validateAccessToken(accessToken)) {
                PreAuthenticatedAuthenticationToken authentication = getAuthentication(accessToken);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException e) {
            response.setStatus(401);
            response.setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "180");

            String message = objectUtil.objectToJson(
                    new ExceptionResponse(messageSourceUtil.getMessage(MessageCode.EXCEPTION_EXPIRED_TOKEN.getCode()),
                            MessageCode.EXCEPTION_EXPIRED_TOKEN.getCode()));
            response.getWriter().write(message);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Optional<String> matchedUri = Arrays.stream(Uris.PERMIT_ALL_URIS)
                .filter(uri -> uri.equals(request.getServletPath()) || "".equals(request.getServletPath()))
                .findFirst();

        return matchedUri.isPresent();
    }

    private String getAccessTokenFromRequest(final HttpServletRequest request) {
        String accessToken = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .orElseThrow(() -> {
                    log.info("Servlet Path is {}", request.getServletPath());
                    return CommonApplicationException.EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND;
                });

        return StringUtil.removeBearerPrefix(accessToken);
    }

    private PreAuthenticatedAuthenticationToken getAuthentication(final String token) {
        UserDetails userDetails = getUserDetails(token);
        return new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private UserDetails getUserDetails(String token) {
        String username = loadUsernameFromTokenUseCase.loadUsernameFromToken(token);
        return userDetailsService.loadUserByUsername(username);
    }
}
