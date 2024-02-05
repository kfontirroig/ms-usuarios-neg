package com.banco.usuarios.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "patron")
@Data
public class UsuarioValidatorPatronesConfig {

    private String email;
    private String password;

}
