package ru.vsu.cs.artfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.vsu.cs.artfolio.auth.AuthenticationService;
import ru.vsu.cs.artfolio.auth.JwtService;
import ru.vsu.cs.artfolio.dto.auth.AuthRequestDto;
import ru.vsu.cs.artfolio.dto.auth.AuthResponseDto;
import ru.vsu.cs.artfolio.dto.auth.RegisterRequestDto;
import ru.vsu.cs.artfolio.user.Role;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false, printOnlyOnFailure = false)
public class AuthenticationControllerTest {

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private static final String VALID_NAME = "username";
    private static final String INVALID_NAME = "ue";
    private static final String VALID_EMAIL = "username@mail.ru";
    private static final String INVALID_EMAIL = "usernamemail.ru";
    private static final String VALID_PASSWORD = "password1";
    private static final String INVALID_PASSWORD = "passw1";

    @Autowired
    public AuthenticationControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void register_ValidInput_Returns200() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);
        AuthResponseDto responseDto = new AuthResponseDto(VALID_NAME, VALID_EMAIL, Role.USER.name(), "token");

        when(authenticationService.register(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(responseDto))
                );
    }

    @Test
    void register_InvalidInput_All_ReturnsStatus400() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto(INVALID_NAME, INVALID_EMAIL, INVALID_PASSWORD);
        register_InvalidInputTestWithJson_ReturnsBadRequest(requestDto);
    }

    @Test
    void register_InvalidInput_Email_ReturnsStatus400() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto(VALID_NAME, INVALID_EMAIL, VALID_PASSWORD);
        register_InvalidInputTestWithJson_ReturnsBadRequest(requestDto);
    }

    @Test
    void register_InvalidInput_Name_ReturnsStatus400() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto(INVALID_NAME, VALID_EMAIL, VALID_PASSWORD);
        register_InvalidInputTestWithJson_ReturnsBadRequest(requestDto);
    }

    @Test
    void register_InvalidInput_Password_ReturnsStatus400() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto(VALID_NAME, VALID_EMAIL, INVALID_PASSWORD);
        register_InvalidInputTestWithJson_ReturnsBadRequest(requestDto);
    }

    private void register_InvalidInputTestWithJson_ReturnsBadRequest(RegisterRequestDto requestDto) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ValidInput_Returns200() throws Exception {
        AuthRequestDto requestDto = new AuthRequestDto(VALID_EMAIL, VALID_PASSWORD);
        AuthResponseDto responseDto = new AuthResponseDto(VALID_NAME, VALID_EMAIL, Role.USER.name(), "token");

        when(authenticationService.authenticate(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(responseDto))
                );
    }
}
