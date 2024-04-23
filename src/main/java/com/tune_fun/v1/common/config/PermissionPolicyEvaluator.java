package com.tune_fun.v1.common.config;

import com.tune_fun.v1.vote.adapter.output.persistence.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionPolicyEvaluator implements PermissionEvaluator {

    private final VoteRepository voteRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        User principal = (User) authentication.getPrincipal();

        if (voteRepository.findById(String.valueOf(targetId)).isEmpty()) {
            log.error("[인가실패] 해당 투표가 존재하지 않습니다. targetId={}", targetId);
            return false;
        }

        return true;
    }
}
