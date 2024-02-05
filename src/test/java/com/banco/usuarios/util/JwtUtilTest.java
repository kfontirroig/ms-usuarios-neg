package com.banco.usuarios.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.banco.usuarios.model.Usuario;
import com.banco.usuarios.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private Usuario usuario;

    private String secretKey = "testSecretKey";
    private int expirationTime = 3600; // in seconds

    @BeforeEach
    public void setup() {
        openMocks(this);
        ReflectionTestUtils.setField(jwtUtil, "SECRET", secretKey);
        ReflectionTestUtils.setField(jwtUtil, "EXPIRATION_TIME", expirationTime);
    }

    @Test
    public void testCreateToken() {
        String testEmail = "test@test.com";
        when(usuario.getEmail()).thenReturn(testEmail);

        String token = jwtUtil.createToken(usuario);

        // Check if the token is properly formed
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
        assertEquals(testEmail, decodedJWT.getSubject());
    }
}
