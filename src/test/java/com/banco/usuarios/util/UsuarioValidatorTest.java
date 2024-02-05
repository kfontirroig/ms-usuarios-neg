package com.banco.usuarios.util;

import com.banco.usuarios.dto.UsuarioCreationDto;
import com.banco.usuarios.exception.CorreoDuplicadoException;
import com.banco.usuarios.exception.FormatoClaveInvalidaException;
import com.banco.usuarios.exception.FormatoCorreoInvalidoException;
import com.banco.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioValidatorTest {

    @Autowired
    private UsuarioValidator usuarioValidator;

    @Test
    public void testValidarCreationUsuarioDtoHappyPath() {
        UsuarioCreationDto dto = new UsuarioCreationDto();
        dto.setEmail("test@test.com");
        dto.setPassword("Password01");
        assertTrue(usuarioValidator.validarCreationUsuarioDto(dto));
    }

    @Test
    public void testValidarCreationUsuarioDtoInvalidEmail() {
        UsuarioCreationDto dto = new UsuarioCreationDto();
        dto.setEmail("invalidEmail");
        dto.setPassword("aPssword01");
        Exception exception = assertThrows(FormatoCorreoInvalidoException.class, () -> {
            usuarioValidator.validarCreationUsuarioDto(dto);
        });
        assertEquals("Correo electronico invalido", exception.getMessage());
    }

    @Test
    public void testValidarCreationUsuarioDtoInvalidPassword() {
        UsuarioCreationDto dto = new UsuarioCreationDto();
        dto.setEmail("test1@test.com");
        dto.setPassword("password");
        Exception exception = assertThrows(FormatoClaveInvalidaException.class, () -> {
            usuarioValidator.validarCreationUsuarioDto(dto);
        });
        assertEquals("La clave es invalida", exception.getMessage());
    }

    @Test
    public void testCrearUsuarioWithExistingEmail() {
        UsuarioCreationDto usuarioCreationDto = UsuarioCreationDto.builder().email("test@test.com").name("test").password("test123").build();
        Usuario usuario = Usuario.builder()
                .email("test@test.com")
                .name("test")
                .password("test123")
                .build();
        assertThrows(CorreoDuplicadoException.class, () -> usuarioValidator.validarCreationUsuarioDto(usuarioCreationDto, usuario));
    }
}
