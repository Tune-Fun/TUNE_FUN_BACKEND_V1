package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.common.entity.BaseEntity;
import com.tune_fun.v1.interaction.adapter.output.persistence.VotePaperLikeJpaEntity;
import com.tune_fun.v1.vote.domain.value.VotePaperOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "vote_paper")
public class VotePaperJpaEntity extends BaseEntity {

    // TODO : TSID Generation으로 변경 필요하나 DummyService.initVotePaperBatch()에서 사용하므로 일단 미적용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("Sequence")
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    @Comment("고유번호")
    private String uuid;

    @Column(name = "title", nullable = false)
    @Comment("제목")
    private String title;

    @Column(name = "content", nullable = false)
    @Comment("내용")
    private String content;

    @Column(name = "page_link")
    @Comment("View 페이지 다이나믹 링크")
    private String pageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, updatable = false, referencedColumnName = "id")
    @Comment("작성자")
    private AccountJpaEntity author;

    @Enumerated(EnumType.STRING)
    @Column(name = "option", nullable = false)
    @Comment("투표 옵션")
    private VotePaperOption option;

    @Column(name = "vote_start_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("투표 시작일")
    private LocalDateTime voteStartAt;

    @Column(name = "vote_end_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("투표 종료일")
    private LocalDateTime voteEndAt;

    @Column(name = "delivery_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("커버 영상 등록일")
    private LocalDateTime deliveryAt;

    @Builder.Default
    @Column(name = "enabled")
    @Comment("사용 여부")
    private boolean enabled = true;

    @Column(name = "video_url")
    @Comment("커버 영상 URL")
    private String videoUrl;

    @Singular
    @OneToMany(mappedBy = "votePaper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VoteChoiceJpaEntity> choices;

    @Singular
    @OneToMany(mappedBy = "votePaper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VotePaperLikeJpaEntity> likes;

    public void disable() {
        this.enabled = false;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        VotePaperJpaEntity that = (VotePaperJpaEntity) object;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
