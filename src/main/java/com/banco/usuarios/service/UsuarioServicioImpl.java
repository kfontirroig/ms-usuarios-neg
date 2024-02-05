package com.banco.usuarios.service;

import com.banco.usuarios.dto.UsuarioCreationDto;
import com.banco.usuarios.dto.UsuarioResponseDto;
import com.banco.usuarios.mapper.UsuarioMapper;
import com.banco.usuarios.model.Usuario;
import com.banco.usuarios.repository.UsuarioRepositorio;
import com.banco.usuarios.util.JwtUtil;
import com.banco.usuarios.util.UsuarioValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Log4j2
@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsuarioValidator usuarioValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio, UsuarioMapper usuarioMapper, BCryptPasswordEncoder passwordEncoder, UsuarioValidator usuarioValidator, JwtUtil jwtUtil) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.usuarioValidator = usuarioValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UsuarioResponseDto crearUsuario(UsuarioCreationDto usuarioCreationDto) {

        log.info("Validando usuario");
        Usuario usuarioEnBD = usuarioRepositorio.findByEmail(usuarioCreationDto.getEmail());
        if (usuarioValidator.validarCreationUsuarioDto(usuarioCreationDto, usuarioEnBD)) {

        Usuario nuevoUsuario = usuarioMapper.fromUsuarioCreationDto(usuarioCreationDto);
        log.debug("Nuevo usuario mapeado: {}", nuevoUsuario);

        //Encriptamos su password
        log.info("Encriptando password");
        nuevoUsuario.setPassword(encryptPassword(nuevoUsuario.getPassword()));

        //Creamos un JWT
        log.info("Generando JWT");
        nuevoUsuario.setToken(jwtUtil.createToken(nuevoUsuario));
        Usuario usuarioGuardado = usuarioRepositorio.save(nuevoUsuario);

        log.info("Nuevo usuario guardado id: " + usuarioGuardado.getId());
        return usuarioMapper.toUsuarioResponseDto(usuarioGuardado);
        }
        return null;
    }

    private String encryptPassword(String rawPassword){
            return passwordEncoder.encode(rawPassword);
        }
}
