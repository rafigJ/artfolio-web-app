package ru.vsu.cs.artfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.vsu.cs.artfolio.dto.auth.AuthRequestDto;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.auth.user.Role;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Sql("/sql/post_service/test_data.sql")
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class SomeControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(SomeControllerTest.class);

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    UserRepository userRepository;

    @Autowired
    public SomeControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        logger.info(String.valueOf(userRepository.count()));
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void hello() throws Exception {
        AuthRequestDto requestDto = new AuthRequestDto("test@gmail.com", "password1");
        String jsonRequestDto = objectMapper.writeValueAsString(requestDto);
        logger.info("JSON Request DTO: " + jsonRequestDto);
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value("Админ Тестовый"),
                        jsonPath("$.email").value("test@gmail.com"),
                        jsonPath("$.role").value(Role.ADMIN.name()),
                        jsonPath("$.token").hasJsonPath()
                );
    }
}