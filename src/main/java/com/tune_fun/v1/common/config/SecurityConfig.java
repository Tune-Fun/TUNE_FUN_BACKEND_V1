package com.tune_fun.v1.common.config;

import com.tune_fun.v1.account.adapter.input.security.JwtAuthenticationFilter;
import com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AuthorizationRequestPersistenceAdapter;
import com.tune_fun.v1.account.application.service.oauth2.CustomOAuth2UserService;
import com.tune_fun.v1.account.application.service.oauth2.decorator.OAuth2AccessTokenResponseClientDecorator;
import com.tune_fun.v1.account.application.service.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.tune_fun.v1.account.application.service.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AccessTokenResponseClientDecorator OAuth2AccessTokenResponseClientDecorator;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthorizationRequestPersistenceAdapter OAuth2AuthorizationRequestPersistenceAdapter;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final PermissionPolicyEvaluator permissionPolicyEvaluator;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("CorsConfigurationSource Activated");
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(asList(HEAD.name(), GET.name(), POST.name(), PUT.name(), DELETE.name(), PATCH.name()));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(asList(COOKIE, ACCEPT, ACCEPT_ENCODING, AUTHORIZATION, CACHE_CONTROL, CONTENT_TYPE));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        log.info("SecurityFilterChain Activated");
        http.headers(headers -> headers
                .xssProtection(Customizer.withDefaults())
                .contentSecurityPolicy(config -> config.policyDirectives("script-src 'self'"))
        );

        http.cors(config -> config.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);

        http
                .sessionManagement(config -> config.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(mvc(introspector, "/static/**", "/favicon.ico")).permitAll()
                        .requestMatchers(mvc(introspector, Uris.PERMIT_ALL_URIS)).permitAll()
                        .requestMatchers(mvc(introspector, Uris.ARTIST_URIS)).hasRole("ARTIST")
                        .requestMatchers(mvc(introspector, Uris.ADMIN_URIS)).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(configure ->
                        configure
                                .tokenEndpoint(config -> config.accessTokenResponseClient(OAuth2AccessTokenResponseClientDecorator))
                                .authorizationEndpoint(config -> config.authorizationRequestRepository(OAuth2AuthorizationRequestPersistenceAdapter))
                                .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        log.info("MethodSecurityExpressionHandler Activated");

        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionPolicyEvaluator);

        return handler;
    }

    private AntPathRequestMatcher[] ant(String... patterns) {
        return stream(patterns).map(this::ant).toArray(AntPathRequestMatcher[]::new);
    }

    private AntPathRequestMatcher ant(String pattern) {
        return new AntPathRequestMatcher(pattern);
    }

    private MvcRequestMatcher[] mvc(HandlerMappingIntrospector introspector, String... patterns) {
        return stream(patterns).map(pattern -> mvc(introspector, pattern)).toArray(MvcRequestMatcher[]::new);
    }

    private MvcRequestMatcher mvc(HandlerMappingIntrospector introspector, String pattern) {
        return new MvcRequestMatcher(introspector, pattern + "/**");
    }

    private MvcRequestMatcher mvc(HandlerMappingIntrospector introspector, HttpMethod method, String pattern) {
        MvcRequestMatcher matcher = new MvcRequestMatcher(introspector, pattern + "/**");
        matcher.setMethod(method);
        return matcher;
    }

}
