package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.github.f4b6a3.ulid.UlidCreator;
import com.tune_fun.v1.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "notification_history")
public class NotificationHistoryJpaEntity extends BaseEntity implements Persistable<String> {

    @Builder.Default
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("ULID")
    private String id = UlidCreator.getMonotonicUlid().toString();

    @Column(name = "target_username", nullable = false, updatable = false)
    @Comment("발송대상 아이디")
    private String targetUsername;

    @Column(name = "title", nullable = false, updatable = false)
    @Comment("알림 제목")
    private String title;

    @Column(name = "content", nullable = false, updatable = false)
    @Comment("알림 내용")
    private String content;

    @Builder.Default
    @Transient
    private boolean isNew = true;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    protected void load() {
        isNew = false;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        NotificationHistoryJpaEntity that = (NotificationHistoryJpaEntity) object;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
