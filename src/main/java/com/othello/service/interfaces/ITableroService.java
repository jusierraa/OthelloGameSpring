package com.othello.service.interfaces;

import com.othello.model.ColorFicha;
import com.othello.model.Posicion;
import com.othello.model.Tablero;
import java.util.List;

/**
 * Interfaz para el servicio de tablero.
 * Cumple con ISP: Define solo operaciones relacionadas con el tablero.
 * Cumple con DIP: Las clases de alto nivel dependen de esta abstracción.
 */
public interface ITableroService {
    
    /**
     * Valida si un movimiento es válido en el tablero.
     */
    boolean esMovimientoValido(Tablero tablero, int fila, int columna, ColorFicha color);
    
    /**
     * Ejecuta un movimiento en el tablero.
     */
    void ejecutarMovimiento(Tablero tablero, int fila, int columna, ColorFicha color);
    
    /**
     * Obtiene todos los movimientos válidos para un color.
     */
    List<Posicion> obtenerMovimientosValidos(Tablero tablero, ColorFicha color);
    
    /**
     * Verifica si un jugador tiene movimientos válidos.
     */
    boolean tieneMovimientosValidos(Tablero tablero, ColorFicha color);
}
