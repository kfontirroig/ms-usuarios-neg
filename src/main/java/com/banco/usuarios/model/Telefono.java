package com.banco.usuarios.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "telefonos")
public class Telefono {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "number")
    private Integer number;

    @NotNull
    @Column(name = "city_code")
    private Integer cityCode;

    @NotNull
    @Column(name = "country_code")
    private Integer countryCode;

    @ManyToOne
    @JoinColumn(name="usuario_id", nullable=false)
    @ToString.Exclude
    private Usuario usuario;
}
