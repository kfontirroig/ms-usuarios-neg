package com.banco.usuarios.service;

import com.banco.usuarios.dto.UsuarioCreationDto;
import com.banco.usuarios.dto.UsuarioResponseDto;


public interface UsuarioServicio {
    UsuarioResponseDto crearUsuario(UsuarioCreationDto usuario);
}
