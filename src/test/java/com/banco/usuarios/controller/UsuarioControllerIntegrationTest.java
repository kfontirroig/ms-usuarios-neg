package com.banco.usuarios.controller;

import com.banco.usuarios.dto.TelefonoDto;
import com.banco.usuarios.dto.UsuarioCreationDto;
import com.banco.usuarios.dto.UsuarioResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioCreationDto usuarioCreationDto;

    @BeforeEach
    public void configurar() {
        // Crear el DTO de usuario
        usuarioCreationDto = new UsuarioCreationDto();
        usuarioCreationDto.setEmail("juan@rodriguez.org");
        usuarioCreationDto.setName("Juan Rodriguez");
        usuarioCreationDto.setPassword("hunt3rX1");

        // Agregar los detalles del telefono
        TelefonoDto telefonoDto = new TelefonoDto();
        telefonoDto.setCitycode(1);
        telefonoDto.setCountrycode(57);
        telefonoDto.setNumber(1234567);

        usuarioCreationDto.setPhones(Collections.singletonList(telefonoDto));
    }

    @Test
    public void pruebaCrearUsuario() throws Exception {
        MvcResult resultadoMvc = mockMvc.perform(post("/usuarios")
                        .content(objectMapper.writeValueAsString(usuarioCreationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Deserializar la respuesta
        String jsonRespuesta = resultadoMvc.getResponse().getContentAsString();
        UsuarioResponseDto respuestaDto = objectMapper.readValue(jsonRespuesta, UsuarioResponseDto.class);

        // Validar la respuesta
        assertNotNull(respuestaDto.getId(), "ID no debe ser nulo");
        assertNotNull(respuestaDto.getCreated(), "Fecha de creacion no debe ser nula");
        assertNotNull(respuestaDto.getLastLogin(), "Ultima fecha de inicio de sesion no debe ser nula");
        assertNotNull(respuestaDto.getToken(), "Token no debe ser nulo");
        assertEquals(true, respuestaDto.getIsActive(), "isActive debe ser verdadero");
    }
}
