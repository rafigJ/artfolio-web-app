package ru.vsu.cs.artfolio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post", schema = "artfolio")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String previewMediaName;

    private String previewType;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<MediaFileEntity> medias;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "owner_uuid")
    private UserEntity owner;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeEntity> likes;
}
