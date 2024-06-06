package ru.vsu.cs.artfolio.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.dto.auth.AuthRequestDto;
import ru.vsu.cs.artfolio.service.PostServiceIT;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(value = "/sql/auth_controller/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class AuthenticationControllerTest {

    private static final InputStream mockFile = PostServiceIT.class.getClassLoader().getResourceAsStream("dummy-image.jpg");
    private static MockMultipartFile mockMultipartFile;

    @BeforeAll
    static void downloadFile() throws Exception {
        mockMultipartFile = new MockMultipartFile("dummy-image.jpg", "dummy-image.jpg", "image/jpeg", mockFile);
    }

    static final Logger LOG = LoggerFactory.getLogger(AuthenticationControllerTest.class);
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @Autowired
    public AuthenticationControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
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
        mockMvc.perform(post("/api/v1/auth/login")
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
    @Disabled
    void register_ValidDto_ReturnAuthResponse() throws Exception {
        // given

        // when

        // then
    }


}
