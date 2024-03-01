package ru.vsu.cs.artfolio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media_file")
public class MediaFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(nullable = false)
    private String type;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Integer position;

}
