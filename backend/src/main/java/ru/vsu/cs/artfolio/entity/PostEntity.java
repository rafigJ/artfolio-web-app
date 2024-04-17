package ru.vsu.cs.artfolio.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.ALL)
    private List<MediaFileEntity> medias;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_uuid")
    private UserEntity owner;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
