package com.banco.usuarios.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.banco.usuarios.model.Usuario;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Log4j2
@Component
public class JwtUtil {


    private String SECRET;
    private int EXPIRATION_TIME;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        SECRET = secret;
    }

    @Value("${jwt.expiration}")
    public void setExpiration(Integer expiration) {
        EXPIRATION_TIME = expiration;
    }

    public String createToken(Usuario usuario) {
        log.info("Generando Token para usuario " + usuario.getEmail());
        return JWT.create()
            .withSubject(usuario.getEmail())
            .withExpiresAt(new Date(System.currentTimeMillis() + this.EXPIRATION_TIME * 1000))
            .sign(Algorithm.HMAC512(this.SECRET));
    }
}