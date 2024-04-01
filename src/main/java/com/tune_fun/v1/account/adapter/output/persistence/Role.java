package com.tune_fun.v1.account.adapter.output.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {

    CLIENT_0("ROLE_CLIENT_0"),
    ARTIST("ROLE_ARTIST"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    public static Set<String> roleValues(Set<Role> roles) {
        return roles.stream().map(Role::name).collect(toSet());
    }

}
