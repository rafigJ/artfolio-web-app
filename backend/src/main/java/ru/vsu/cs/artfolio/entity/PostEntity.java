package ru.vsu.cs.artfolio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "post")
    private List<MediaFileEntity> medias;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "owner_uuid")
    private UserEntity owner;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "post")
    private List<LikeEntity> likes;

    @Column(name = "deleted")
    private Boolean deleted;
}
