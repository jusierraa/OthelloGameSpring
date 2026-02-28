package com.othello.exception;

/**
 * Excepción lanzada cuando un movimiento no es válido.
 */
public class MovimientoInvalidoException extends RuntimeException {
    public MovimientoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
