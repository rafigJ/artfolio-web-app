package ru.vsu.cs.artfolio.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.dto.auth.AuthRequestDto;
import ru.vsu.cs.artfolio.dto.auth.ChangePasswordRequestDto;
import ru.vsu.cs.artfolio.dto.auth.RegisterRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.PostServiceIT;
import ru.vsu.cs.artfolio.service.impl.MinioService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(value = "/sql/auth_controller/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/sql/auth_controller/test_data_update.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class AuthenticationControllerTest {

    private static final InputStream mockFile = PostServiceIT.class.getClassLoader().getResourceAsStream("dummy-image.jpg");
    private static final String AUTH_PATH = "/api/v1/auth";
    private static MockMultipartFile mockMultipartFile;

    @BeforeAll
    static void downloadFile() throws Exception {
        mockMultipartFile = new MockMultipartFile("dummy-image.jpg", "dummy-image.jpg", "image/jpeg", mockFile);
    }

    static final Logger LOG = LoggerFactory.getLogger(AuthenticationControllerTest.class);
    UserRepository userRepository;
    MinioService minioService;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @Autowired
    public AuthenticationControllerTest(UserRepository userRepository, MinioService minioService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.minioService = minioService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }


    @Test
    void login_ValidLoginRequest_ReturnAuthResponse() throws Exception {
        // given
        AuthRequestDto requestDto = new AuthRequestDto("bolton@vesteros.com", "somePassword19");
        String jsonRequestDto = objectMapper.writeValueAsString(requestDto);
        LOG.info("json request {}", jsonRequestDto);

        // when
        mockMvc.perform(post(AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto))
                // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value("Рамси Болтон"),
                        jsonPath("$.email").value("bolton@vesteros.com"),
                        jsonPath("$.role").value(Role.USER.name()),
                        jsonPath("$.token").hasJsonPath()
                );
    }


    @Test
    void register_ValidDto_ReturnAuthResponse() throws Exception {
        // given
        RegisterRequestDto requestDto = new RegisterRequestDto(
                "full name",
                "description",
                "country",
                "city",
                "username",
                "email@gmail.com",
                "passwordSome12",
                "secretWord"
        );
        String jsonRequestDto = objectMapper.writeValueAsString(requestDto);
        LOG.info("json request {}", jsonRequestDto);

        // when
        mockMvc.perform(
                        multipart(AUTH_PATH + "/register")
                                .part(new MockPart("avatarFile", "some file_name",
                                        mockMultipartFile.getBytes(),
                                        MediaType.IMAGE_JPEG)
                                )
                                .part(new MockPart("userInfo", null,
                                        jsonRequestDto.getBytes(StandardCharsets.UTF_8),
                                        MediaType.APPLICATION_JSON)
                                )
                                .content(jsonRequestDto)
                )
                // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.username").value("username"),
                        jsonPath("$.name").value("full name"),
                        jsonPath("$.email").value("email@gmail.com"),
                        jsonPath("$.role").value(Role.USER.name()),
                        jsonPath("$.token").hasJsonPath()
                );
        // after remove trash
        clearMinio();
    }

    @Test
    void changePassword_ValidChangePasswordRequest_Return200() throws Exception {
        // given
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto("bolton@vesteros.com", "winterIsComing", "somePassword12");
        String jsonRequestDto = objectMapper.writeValueAsString(requestDto);
        LOG.info("json request {}", jsonRequestDto);

        // when
        mockMvc.perform(
                        patch(AUTH_PATH + "/change-password")
                                .content(jsonRequestDto)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // then
                .andExpect(status().isOk());
    }

    @Test
    void loginAfterChangePassword_ValidChangePasswordRequest_ReturnAuthResponse() throws Exception {
        // given
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto("bolton@vesteros.com", "winterIsComing", "somePassword12");
        String jsonRequestDto = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        patch(AUTH_PATH + "/change-password")
                                .content(jsonRequestDto)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // then
                .andExpect(status().isOk());

        AuthRequestDto authRequestDto = new AuthRequestDto("bolton@vesteros.com", "somePassword12");
        String authJsonRequest = objectMapper.writeValueAsString(authRequestDto);

        // when
        mockMvc.perform(post(AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJsonRequest))
                // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value("Рамси Болтон"),
                        jsonPath("$.email").value("bolton@vesteros.com"),
                        jsonPath("$.role").value(Role.USER.name()),
                        jsonPath("$.token").hasJsonPath()
                );
    }

    private void clearMinio() {
        var mediaFiles = userRepository.findAll().parallelStream().map(UserEntity::getAvatarName).toList();
        minioService.deleteFiles(mediaFiles);
    }

}
