package com.othello.service.interfaces;

import com.othello.model.Posicion;
import com.othello.model.Tablero;
import com.othello.model.ColorFicha;

/**
 * Interfaz para estrategias de jugador (Strategy Pattern).
 * Cumple con OCP: Permite agregar nuevas estrategias sin modificar código existente.
 * Cumple con ISP: Define solo la operación necesaria para un jugador.
 * Cumple con DIP: Las clases de alto nivel dependen de esta abstracción.
 */
public interface IJugadorStrategy {
    
    /**
     * Calcula y retorna el siguiente movimiento del jugador.
     */
    Posicion calcularMovimiento(Tablero tablero, ColorFicha color);
}
