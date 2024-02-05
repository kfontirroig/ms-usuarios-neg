package com.banco.usuarios.repository;

import com.banco.usuarios.model.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TelefonoRepositorio extends JpaRepository<Telefono, UUID> {
}
