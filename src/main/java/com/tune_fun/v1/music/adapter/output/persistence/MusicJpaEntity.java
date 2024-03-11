package com.tune_fun.v1.music.adapter.output.persistence;

import com.tune_fun.v1.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "music")
public class MusicJpaEntity extends BaseEntity {

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

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    @Comment("제목")
    private String title;

    @Size(max = 255)
    @NotNull
    @Column(name = "artist", nullable = false)
    @Comment("아티스트")
    private String artist;

    @Size(max = 255)
    @NotNull
    @Column(name = "genre", nullable = false)
    @Comment("장르")
    private String genre;

    @NotNull
    @Size(max = 255)
    @Column(name = "album_name", nullable = false)
    @Comment("앨범 이름")
    private String albumName;

    @Size(max = 255)
    @NotNull
    @Column(name = "album_image_link", nullable = false)
    @Comment("앨범 이미지 링크")
    private String albumImageLink;

    @Column(name = "released_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("공개일")
    private LocalDateTime releasedAt;

}
