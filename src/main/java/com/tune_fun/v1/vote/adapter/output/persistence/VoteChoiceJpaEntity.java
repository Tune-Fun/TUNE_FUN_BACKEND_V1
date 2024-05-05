package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.Objects;

@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "vote_choice")
public class VoteChoiceJpaEntity extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("TSID")
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    @Comment("고유번호")
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vote_paper_id", nullable = false, updatable = false, referencedColumnName = "id")
    @Comment("투표 게시물 ID")
    private VotePaperJpaEntity votePaper;

    @JdbcTypeCode(SqlTypes.JSON)
    @Embedded
    @Comment("선택지 제안사항")
    private Offer offer;

    @Singular
    @OneToMany(mappedBy = "voteChoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VoteJpaEntity> votes;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    private String createdBy;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        VoteChoiceJpaEntity that = (VoteChoiceJpaEntity) object;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
