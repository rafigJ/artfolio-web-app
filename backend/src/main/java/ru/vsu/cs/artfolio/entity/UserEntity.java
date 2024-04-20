package ru.vsu.cs.artfolio.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.vsu.cs.artfolio.auth.user.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, name = "uuid")
    private UUID uuid;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, unique = true, name = "username")
    private String username;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false)
    private String secretWord;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Lob
    @Column(name = "avatar_file")
    @Basic(fetch = FetchType.LAZY)
    private byte[] avatar;
    // для работы ленивой загрузки, необходимо создать ещё одну сущность, оборачивающее данное поле avatar

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role;

    @Column(nullable = false, name = "create_time")
    private LocalDateTime createTime;

    @Column(nullable = false, name = "update_time")
    private LocalDateTime updateTime;

}
