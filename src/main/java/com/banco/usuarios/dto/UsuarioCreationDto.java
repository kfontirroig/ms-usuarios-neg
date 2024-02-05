package com.banco.usuarios.dto;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UsuarioCreationDto {
    private String email;
    private String name;
    private String password;
    private List<TelefonoDto> phones;


}
