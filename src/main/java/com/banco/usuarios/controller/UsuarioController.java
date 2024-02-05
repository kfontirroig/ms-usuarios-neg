package com.banco.usuarios.controller;

import com.banco.usuarios.dto.UsuarioCreationDto;
import com.banco.usuarios.dto.UsuarioResponseDto;
import com.banco.usuarios.service.UsuarioServicio;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServicio usuarioServicio;

    @Autowired
    public UsuarioController(UsuarioServicio usuarioServicio){
        this.usuarioServicio = usuarioServicio;
    }


    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crearUsuario(@RequestBody UsuarioCreationDto usuarioCreationDto){
        log.info("Iniciando creacion de usuario con datos: { email: " + usuarioCreationDto.getEmail() + ", nombre: " + usuarioCreationDto.getName() );
        UsuarioResponseDto usuarioCreado = usuarioServicio.crearUsuario(usuarioCreationDto);
        log.info("Usuario creado exitosamente con Id: " + usuarioCreado.getId());
        return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
    }
}