package com.banco.usuarios.exception;

public class CorreoDuplicadoException extends RuntimeException {
    public CorreoDuplicadoException(String message){
        super(message);
    }
}
