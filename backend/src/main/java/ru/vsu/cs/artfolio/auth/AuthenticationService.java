package ru.vsu.cs.artfolio.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.dto.auth.AuthRequestDto;
import ru.vsu.cs.artfolio.dto.auth.AuthResponseDto;
import ru.vsu.cs.artfolio.dto.auth.ChangePasswordRequestDto;
import ru.vsu.cs.artfolio.dto.auth.RegisterRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.ExistUserException;
import ru.vsu.cs.artfolio.exception.IncorrectCredentialsException;
import ru.vsu.cs.artfolio.exception.NotExistUserException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto request, MultipartFile avatarFile) {
        if (repository.existsByEmailOrUsername(request.email(), request.username())) {
            throw new ExistUserException();
        }

        try {
            UserEntity userEntity = convertRequestToEntity(request, avatarFile, passwordEncoder);
            repository.saveAndFlush(userEntity);
            User user = new User(userEntity);
            String jwtToken = jwtService.generateToken(user);
            return convertEntityToAuthResponse(userEntity, jwtToken);
        } catch (DataIntegrityViolationException | IOException e) {
            throw new RestException("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public AuthResponseDto authenticate(AuthRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException e) {
            throw new IncorrectCredentialsException();
        }
        UserEntity userEntity = repository.findByEmail(request.email())
                .orElseThrow(NotExistUserException::new);

        User user = new User(userEntity);
        String jwtToken = jwtService.generateToken(user);
        return convertEntityToAuthResponse(userEntity, jwtToken);
    }

    public void changePassword(ChangePasswordRequestDto requestDto) {
        UserEntity user = repository.findByEmail(requestDto.email())
                .orElseThrow(IncorrectCredentialsException::new);

        if (!passwordEncoder.matches(requestDto.secretWord(), user.getSecretWord())) {
            throw new IncorrectCredentialsException();
        }

        user.setPassword(passwordEncoder.encode(requestDto.newPassword()));
        repository.save(user);
    }

    private static AuthResponseDto convertEntityToAuthResponse(UserEntity userEntity, String token) {
        return AuthResponseDto.builder()
                .name(userEntity.getFullName())
                .email(userEntity.getEmail())
                .role(userEntity.getRole().name())
                .token(token)
                .build();
    }

    private static UserEntity convertRequestToEntity(RegisterRequestDto request, MultipartFile avatar, PasswordEncoder passwordEncoder) throws IOException {
        return UserEntity.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .secretWord(passwordEncoder.encode(request.secretWord()))
                .fullName(request.fullName())
                .country(request.country())
                .city(request.city())
                .additionalInfo(request.description())
                .avatar(avatar.getBytes())
                .avatarType(avatar.getContentType())
                .role(Role.USER)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
