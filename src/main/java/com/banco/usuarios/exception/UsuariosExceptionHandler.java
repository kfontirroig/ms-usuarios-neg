package com.banco.usuarios.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import javax.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class UsuariosExceptionHandler {

    @ExceptionHandler({FormatoCorreoInvalidoException.class, FormatoClaveInvalidaException.class})
    public ResponseEntity<MensajeError> manejarExcepcionesValidacion(RuntimeException e) {
        String error = e.getMessage();
        log.error("Excepcion de validacion: {}", error, e);
        return new ResponseEntity<>(new MensajeError(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<MensajeError> manejarExcepcionCorreoExistente(CorreoDuplicadoException e) {
        String error = e.getMessage();
        log.error("Excepcion de correo ya registrado: {}", error, e);
        return new ResponseEntity<>(new MensajeError(error), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MensajeError> manejarExcepcionViolacionRestricciones(ConstraintViolationException e) {
        String error = e.getConstraintViolations().stream()
                .map(violacion -> violacion.getPropertyPath() + " " + violacion.getMessage())
                .collect(Collectors.joining(", "));
        log.error("Excepcion de violacion de restricciones: {}", error, e);
        return new ResponseEntity<>(new MensajeError(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeError> manejarExcepcionGeneral(Exception e) {
        String error = e.getMessage();
        log.error("Excepcion : {}", error, e);
        return new ResponseEntity<>(new MensajeError(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MensajeError> manejarExcepcionMensajeHttpNoLeible(HttpMessageNotReadableException e) {
        Throwable causa = e.getCause();

        if (causa instanceof InvalidFormatException) {
            return manejarFormatoInvalido((InvalidFormatException) causa);
        }

        String error = e.getMessage();
        log.error("Excepcion: {}", error, e);
        return new ResponseEntity<>(new MensajeError(error), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<MensajeError> manejarFormatoInvalido(InvalidFormatException e) {
        String nombrePropiedad = e.getPath().get(e.getPath().size()-1).getFieldName();
        String valorPropiedad = e.getValue().toString();
        String tipoEsperado = e.getTargetType().getSimpleName();

        String mensajeRespuesta = String.format("La propiedad '%s': '%s' debe ser un %s", nombrePropiedad, valorPropiedad, tipoEsperado.toLowerCase());
        log.error("Excepcion de serializacion de JSON: {}", mensajeRespuesta, e);
        return new ResponseEntity<>(new MensajeError(mensajeRespuesta), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<MensajeError> manejarExcepcionTransaccion(TransactionSystemException e) {
        Throwable causa = e.getRootCause();

        if (causa instanceof ConstraintViolationException) {
            return manejarExcepcionViolacionRestricciones((ConstraintViolationException) causa);
        }

        return manejarExcepcionGeneral(e);
    }

}
