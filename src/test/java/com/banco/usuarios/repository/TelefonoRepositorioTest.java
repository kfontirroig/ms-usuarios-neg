package com.banco.usuarios.repository;

import com.banco.usuarios.model.Telefono;
import com.banco.usuarios.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TelefonoRepositorioTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TelefonoRepositorio telefonoRepositorio;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFindById() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setName("Test");
        usuario.setPassword("Test123");

        entityManager.persist(usuario);
        entityManager.flush();

        Telefono telefono = new Telefono();
        telefono.setNumber(123456789);
        telefono.setCityCode(1);
        telefono.setCountryCode(1);
        telefono.setUsuario(usuario);

        entityManager.persist(telefono);
        entityManager.flush();

        Telefono found = telefonoRepositorio.findById(telefono.getId()).orElse(null);

        assertEquals(telefono.getId(), found.getId());
    }
}
