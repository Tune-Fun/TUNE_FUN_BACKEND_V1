package com.tune_fun.v1.account.adapter.output.persistence.oauth2;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
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

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "oauth2_account")
public class OAuth2AccountJpaEntity extends BaseEntity {

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

    @NotNull
    @Column(name = "email", nullable = false, updatable = false)
    @Comment("이메일")
    private String email;

    @NotNull
    @Column(name = "nickname", nullable = false)
    @Comment("닉네임")
    private String nickname;

    @NotNull
    @Column(name = "oauth2_provider", nullable = false)
    @Comment("OAuth2 플랫폼")
    private String oauth2Provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, referencedColumnName = "id")
    private AccountJpaEntity account;

    @Builder.Default
    @Column(name = "is_enabled")
    private Boolean isEnabled = true;


}
