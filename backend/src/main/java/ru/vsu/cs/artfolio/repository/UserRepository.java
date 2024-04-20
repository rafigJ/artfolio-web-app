package ru.vsu.cs.artfolio.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
