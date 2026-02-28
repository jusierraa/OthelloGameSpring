package com.othello.service.interfaces;

import com.othello.dto.CrearJuegoDTO;
import com.othello.dto.JuegoDTO;
import com.othello.dto.MovimientoDTO;
import com.othello.model.Juego;
import java.util.List;

/**
 * Interfaz para el servicio de juego.
 * Cumple con ISP: Define solo operaciones relacionadas con la gestión del juego.
 * Cumple con DIP: Las clases de alto nivel dependen de esta abstracción.
 */
public interface IJuegoService {
    
    /**
     * Crea una nueva partida.
     */
    JuegoDTO crearJuego(CrearJuegoDTO crearJuegoDTO);
    
    /**
     * Obtiene el estado actual de un juego.
     */
    JuegoDTO obtenerJuego(String juegoId);
    
    /**
     * Realiza un movimiento en el juego.
     */
    JuegoDTO realizarMovimiento(String juegoId, MovimientoDTO movimiento);
    
    /**
     * Obtiene todos los juegos activos.
     */
    List<JuegoDTO> obtenerTodosLosJuegos();
    
    /**
     * Elimina un juego.
     */
    void eliminarJuego(String juegoId);
    
    /**
     * Convierte un objeto Juego a JuegoDTO.
     */
    JuegoDTO convertirADTO(Juego juego);
}
