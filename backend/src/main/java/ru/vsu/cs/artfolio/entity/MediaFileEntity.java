package ru.vsu.cs.artfolio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media_file", schema = "artfolio")
public class MediaFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Column(nullable = false)
    private String type;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Integer position;

}
