package com.banco.usuarios.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Email
    @Column(name = "email", unique=true)
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified")
    private Date modified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "token")
    private String token;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Telefono> phones;

    @PrePersist
    public void prePersist() {
        created = new Date();
        modified = new Date();
        lastLogin = new Date();
        isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        modified = new Date();
    }
}
