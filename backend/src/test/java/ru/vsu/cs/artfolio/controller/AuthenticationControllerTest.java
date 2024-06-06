package ru.vsu.cs.artfolio.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.dto.auth.AuthRequestDto;
import ru.vsu.cs.artfolio.dto.auth.RegisterRequestDto;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.repository.MediaRepository;
import ru.vsu.cs.artfolio.service.PostServiceIT;
import ru.vsu.cs.artfolio.service.impl.MinioService;

import java.io.InputStream;
import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(value = "/sql/auth_controller/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
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

    MediaRepository mediaRepository;
    MinioService minioService;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @Autowired
    public AuthenticationControllerTest(MediaRepository mediaRepository, MinioService minioService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mediaRepository = mediaRepository;
        this.minioService = minioService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @AfterEach
    void clearMinio() {
        var mediaFiles = mediaRepository.findAll().stream().map(MediaFileEntity::getFileName).toList();
        minioService.deleteFiles(mediaFiles);
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
                        .file("file", mockMultipartFile.getBytes())
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
    }


}
