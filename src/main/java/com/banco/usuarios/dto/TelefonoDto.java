package com.banco.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefonoDto {
    private Integer citycode;
    private Integer countrycode;
    private Integer number;
}