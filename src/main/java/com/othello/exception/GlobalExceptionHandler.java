package com.othello.exception;

import com.othello.dto.RespuestaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 * Cumple con SRP: Solo maneja excepciones y genera respuestas apropiadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JuegoNoEncontradoException.class)
    public ResponseEntity<RespuestaDTO> handleJuegoNoEncontrado(JuegoNoEncontradoException ex) {
        RespuestaDTO respuesta = new RespuestaDTO(false, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(MovimientoInvalidoException.class)
    public ResponseEntity<RespuestaDTO> handleMovimientoInvalido(MovimientoInvalidoException ex) {
        RespuestaDTO respuesta = new RespuestaDTO(false, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(VersionConflictException.class)
    public ResponseEntity<Map<String, Object>> handleVersionConflict(VersionConflictException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "version_conflict");
        response.put("message", ex.getMessage());
        response.put("expectedVersion", ex.getExpectedVersion());
        response.put("currentVersion", ex.getCurrentVersion());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RespuestaDTO> handleAccessDenied(AccessDeniedException ex) {
        RespuestaDTO respuesta = new RespuestaDTO(false, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });
        RespuestaDTO respuesta = new RespuestaDTO(false, "Errores de validación", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaDTO> handleGeneralException(Exception ex) {
        RespuestaDTO respuesta = new RespuestaDTO(false, "Error interno del servidor: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
