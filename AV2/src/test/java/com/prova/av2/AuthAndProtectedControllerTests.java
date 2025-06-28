package com.prova.av2;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.prova.av2.service.AuthService;
import com.prova.av2.service.JwtService;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(properties = {
  "springdoc.api-docs.enabled=false",
  "springdoc.swagger-ui.enabled=false",
  "jwt.secret=testsecret",
  "jwt.expiration=3600000"
})
public class AuthAndProtectedControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    // --- TESTES PARA AuthController ---

    @Test
    void login_Success_ReturnsToken() throws Exception {
        String username = "user1";
        String password = "1234";
        String token = "fake.jwt.token";

        when(authService.authenticateUserAndGenerateToken(username, password)).thenReturn(token);

        mockMvc.perform(post("/auth/login")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(content().string(token));

        verify(authService, times(1)).authenticateUserAndGenerateToken(username, password);
    }

    @Test
    void login_BadCredentials_Returns401() throws Exception {
        String username = "user1";
        String password = "wrong";

        when(authService.authenticateUserAndGenerateToken(username, password))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Credenciais inválidas"));

        verify(authService, times(1)).authenticateUserAndGenerateToken(username, password);
    }

    @Test
    void validateToken_Valid_Returns200() throws Exception {
        String token = "valid.token";
        String username = "user1";

        when(jwtService.validateToken(token)).thenReturn(true);
        when(jwtService.getUsernameFromToken(token)).thenReturn(username);

        mockMvc.perform(post("/auth/validate")
                .param("token", token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(content().string("Token válido! Username: " + username));

        verify(jwtService, times(1)).validateToken(token);
        verify(jwtService, times(1)).getUsernameFromToken(token);
    }

    @Test
    void validateToken_Invalid_Returns401() throws Exception {
        String token = "invalid.token";

        when(jwtService.validateToken(token)).thenReturn(false);

        mockMvc.perform(post("/auth/validate")
                .param("token", token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Token inválido ou expirado."));

        verify(jwtService, times(1)).validateToken(token);
        verify(jwtService, never()).getUsernameFromToken(anyString());
    }

    // --- TESTES PARA TestProtectedController ---

    @Test
    @WithMockUser
    void accessHello_AsAuthenticatedUser_ReturnsOk() throws Exception {
        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk())
            .andExpect(content().string("Olá! Você acessou um endpoint protegido com sucesso!"));
    }

    @Test
    void accessHello_WithoutAuthentication_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/hello"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void accessAdmin_AsUserRole_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/admin"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void accessAdmin_AsAdminRole_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin"))
            .andExpect(status().isOk())
            .andExpect(content().string("Bem-vindo, Administrador! Este é um recurso restrito."));
    }
}
