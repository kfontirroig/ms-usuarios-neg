package com.banco.usuarios.service;

import com.banco.usuarios.dto.UsuarioCreationDto;
import com.banco.usuarios.dto.UsuarioResponseDto;
import com.banco.usuarios.mapper.UsuarioMapper;
import com.banco.usuarios.model.Usuario;
import com.banco.usuarios.repository.UsuarioRepositorio;
import com.banco.usuarios.util.JwtUtil;
import com.banco.usuarios.util.UsuarioValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UsuarioServicioImplTest {

    @InjectMocks
    UsuarioServicioImpl usuarioServicio;

    @Mock
    UsuarioRepositorio usuarioRepositorio;

    @Mock
    UsuarioMapper usuarioMapper;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UsuarioValidator usuarioValidator;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(usuarioRepositorio.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario userToSave = invocation.getArgument(0);
            userToSave.setId(UUID.randomUUID());
            userToSave.setCreated(new Date());
            userToSave.setModified(new Date());
            userToSave.setLastLogin(new Date());
            userToSave.setIsActive(true);
            return userToSave;
        });

        //Mocking el mapper
        when(usuarioMapper.fromUsuarioCreationDto(any(UsuarioCreationDto.class))).thenAnswer(invocation -> {
            UsuarioCreationDto usuarioDto = invocation.getArgument(0);
            UsuarioMapper mapper = new UsuarioMapper();
            return mapper.fromUsuarioCreationDto(usuarioDto);
        });
        when(usuarioMapper.toUsuarioResponseDto(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            UsuarioMapper mapper = new UsuarioMapper();
            return mapper.toUsuarioResponseDto(usuario);
        });

        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");

        when(jwtUtil.createToken(any(Usuario.class))).thenReturn("ThisIsAToken");

    }

    @Test
    public void testCrearUsuario() {
        UsuarioCreationDto usuarioCreationDto = UsuarioCreationDto.builder().email("test@test.com").name("TestUser").password("Test1234").build();
        when(usuarioValidator.validarCreationUsuarioDto(any(UsuarioCreationDto.class), eq(null))).thenReturn(true);
        when(usuarioRepositorio.findByEmail(any(String.class))).thenReturn(null);
        UsuarioResponseDto result = usuarioServicio.crearUsuario(usuarioCreationDto);

        assertNotNull(result.getToken());
        assertTrue(result.getIsActive());
    }



}
