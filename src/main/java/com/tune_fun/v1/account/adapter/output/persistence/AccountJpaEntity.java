package com.tune_fun.v1.account.adapter.output.persistence;

import com.tune_fun.v1.common.converter.EncryptConverter;
import com.tune_fun.v1.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "account")
public class AccountJpaEntity extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("Sequence")
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "uuid", nullable = false)
    @Comment("고유번호")
    private String uuid;

    @Convert(converter = EncryptConverter.class)
    @Size(max = 255)
    @Column(name = "username", nullable = false)
    @Comment("아이디")
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    @Comment("비밀번호")
    private String password;

    @Convert(converter = EncryptConverter.class)
    @NotNull
    @Column(name = "email", nullable = false)
    @Comment("이메일")
    private String email;

    @Convert(converter = EncryptConverter.class)
    @NotNull
    @Column(name = "nickname", nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Builder.Default
    @Embedded
    private NotificationConfig notificationConfig = new NotificationConfig();

    @Builder.Default
    @Convert(converter = RoleConverter.class)
    @Column(name = "roles", nullable = false)
    private List<Role> roles = new ArrayList<>();

    @Column(name = "last_login_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("최종 로그인 일시")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_logout_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("최종 로그아웃 일시")
    private LocalDateTime lastLogoutAt;

    @Column(name = "email_verified_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("이메일 인증 일시")
    private LocalDateTime emailVerifiedAt;

    @Column(name = "withdrawal_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("탈퇴 일시")
    private LocalDateTime withdrawalAt;

    @Column(name = "deleted_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("삭제일")
    protected LocalDateTime deletedAt;

    @Builder.Default
    @Column(name = "is_account_non_expired")
    private Boolean isAccountNonExpired = true;

    @Builder.Default
    @Column(name = "is_account_non_locked")
    private Boolean isAccountNonLocked = true;

    @Builder.Default
    @Column(name = "is_credentials_non_expired")
    private Boolean isCredentialsNonExpired = true;

    @Builder.Default
    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isAvailable() {
        return !isAccountNonExpired && !isAccountNonLocked && !isCredentialsNonExpired && !isEnabled;
    }

}
