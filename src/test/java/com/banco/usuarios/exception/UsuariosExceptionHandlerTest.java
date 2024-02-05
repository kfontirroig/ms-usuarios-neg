package com.banco.usuarios.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.transaction.TransactionSystemException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class UsuariosExceptionHandlerTest {

    @InjectMocks
    private UsuariosExceptionHandler usuariosExceptionHandler;

    @Mock
    private Exception excepcion;

    @BeforeEach
    public void preparar() {
        openMocks(this);
    }

    @Test
    void deberiaManejarExcepcionesValidacion() {
        FormatoCorreoInvalidoException excepcion = new FormatoCorreoInvalidoException("Correo electronico invalido");
        ResponseEntity<MensajeError> respuesta = usuariosExceptionHandler.manejarExcepcionesValidacion(excepcion);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Correo electronico invalido", respuesta.getBody().getMensaje());
    }

    @Test
    void deberiaManejarExcepcionCorreoExistente() {
        CorreoDuplicadoException excepcion = new CorreoDuplicadoException("El correo ya esta registrado");
        ResponseEntity<MensajeError> respuesta = usuariosExceptionHandler.manejarExcepcionCorreoExistente(excepcion);

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("El correo ya esta registrado", respuesta.getBody().getMensaje());
    }

    @Test
    void deberiaManejarExcepcionViolacionRestricciones() {
        Set<ConstraintViolation<?>> violaciones = new HashSet<>();
        ConstraintViolation<?> violacion = mock(ConstraintViolation.class);
        when(violacion.getPropertyPath()).thenReturn(PathImpl.createPathFromString("campo"));
        when(violacion.getMessage()).thenReturn("mensaje error");
        violaciones.add(violacion);

        ConstraintViolationException excepcion = new ConstraintViolationException(violaciones);
        ResponseEntity<MensajeError> respuesta = usuariosExceptionHandler.manejarExcepcionViolacionRestricciones(excepcion);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("campo mensaje error", respuesta.getBody().getMensaje());
    }

    @Test
    void deberiaManejarExcepcionGeneral() {
        String mensajeError = "Error de prueba";
        when(excepcion.getMessage()).thenReturn(mensajeError);

        ResponseEntity<MensajeError> resultado = usuariosExceptionHandler.manejarExcepcionGeneral(excepcion);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resultado.getBody().getMensaje()).isEqualTo(mensajeError);
    }

    @Test
    void deberiaManejarExcepcionMensajeHttpNoLeible() {
        JsonMappingException.Reference ref = new JsonMappingException.Reference(new Object(), "campo");
        List<JsonMappingException.Reference> path = Collections.singletonList(ref);

        InvalidFormatException excepcion = new InvalidFormatException(null, "mensaje error", "valor invalido", Integer.class);
        excepcion.prependPath(ref);

        ResponseEntity<MensajeError> respuesta = usuariosExceptionHandler.manejarExcepcionMensajeHttpNoLeible(new HttpMessageNotReadableException("mensaje error", excepcion, new MockHttpInputMessage(new byte[0])));

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("La propiedad 'campo': 'valor invalido' debe ser un integer", respuesta.getBody().getMensaje());
    }

    @Test
    void deberiaManejarExcepcionTransaccion() {
        TransactionSystemException excepcion = new TransactionSystemException("mensaje error");
        ResponseEntity<MensajeError> respuesta = usuariosExceptionHandler.manejarExcepcionTransaccion(excepcion);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertEquals("mensaje error", respuesta.getBody().getMensaje());
    }
}
