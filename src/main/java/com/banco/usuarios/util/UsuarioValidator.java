package com.banco.usuarios.util;

import com.banco.usuarios.config.UsuarioValidatorPatronesConfig;
import com.banco.usuarios.dto.UsuarioCreationDto;
import com.banco.usuarios.exception.CorreoDuplicadoException;
import com.banco.usuarios.exception.FormatoCorreoInvalidoException;
import com.banco.usuarios.exception.FormatoClaveInvalidaException;
import com.banco.usuarios.model.Usuario;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Component
@Log4j2
public class UsuarioValidator {

    private final UsuarioValidatorPatronesConfig patronConfig;

    private final Pattern EMAIL_PATTERN;
    private final Pattern PASSWORD_PATTERN;

    @Autowired
    public UsuarioValidator(UsuarioValidatorPatronesConfig patronConfig){
        this.patronConfig = patronConfig;

        EMAIL_PATTERN = Pattern.compile(this.patronConfig.getEmail());
        log.info("PatrFon compilado, Email: {}", EMAIL_PATTERN.pattern());

        PASSWORD_PATTERN = Pattern.compile(this.patronConfig.getPassword());
        log.info("Patron compilado, Contraseña: {}", PASSWORD_PATTERN.pattern());
    }

    public boolean validarCreationUsuarioDto(UsuarioCreationDto usuarioCreationDto, Usuario usuarioEnBD){
        log.info("Validando el formato del correo: {}", usuarioCreationDto.getEmail());
        Matcher emailMatcher = EMAIL_PATTERN.matcher(usuarioCreationDto.getEmail());
        if (!emailMatcher.matches()) {
            throw new FormatoCorreoInvalidoException("Correo electronico invalido");
        }

        log.info("Validando que el correo {} no este registrado", usuarioCreationDto.getEmail());
        if (usuarioEnBD != null) {
            throw new CorreoDuplicadoException("El correo ya esta registrado");
        }

        log.info("Validando el formato de la contraseña");
        Matcher passwordMatcher = PASSWORD_PATTERN.matcher(usuarioCreationDto.getPassword());
        if (!passwordMatcher.matches()) {
            throw new FormatoClaveInvalidaException("La clave es invalida");
        }
        return true;
    }

    public boolean validarCreationUsuarioDto(UsuarioCreationDto usuarioCreationDto){
        return validarCreationUsuarioDto(usuarioCreationDto, null);
    }
}
