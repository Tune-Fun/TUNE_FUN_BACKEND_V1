package com.tune_fun.v1.account.adapter.output.persistence.device;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "device")
public class DeviceJpaEntity extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, updatable = false, referencedColumnName = "id")
    private AccountJpaEntity account;

    @Size(max = 255)
    @NotNull
    @Column(name = "fcm_token", nullable = false)
    @Comment("FCM 토큰")
    private String fcmToken;

    @Size(max = 255)
    @NotNull
    @Column(name = "device_token", nullable = false)
    @Comment("디바이스 토큰")
    private String deviceToken;

}
