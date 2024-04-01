package com.tune_fun.v1.account.domain.state;

import java.util.Set;

public interface Account {
    String username();

    Set<String> roles();
}
