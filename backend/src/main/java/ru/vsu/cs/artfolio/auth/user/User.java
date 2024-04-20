package ru.vsu.cs.artfolio.auth.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.util.Collection;
import java.util.List;

@Getter
public class User implements UserDetails {
    private final UserEntity userEntity;

    public User(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userEntity.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    // TODO поменять на username
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
