package ru.vsu.cs.artfolio.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmailOrUsername(String email, String username);

    void removeByUsername(String username);
}
