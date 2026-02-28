package com.othello.exception;

/**
 * Excepción lanzada cuando un juego no es encontrado.
 */
public class JuegoNoEncontradoException extends RuntimeException {
    public JuegoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
