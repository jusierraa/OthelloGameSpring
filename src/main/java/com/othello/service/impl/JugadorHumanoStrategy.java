package com.othello.service.impl;

import com.othello.model.ColorFicha;
import com.othello.model.Posicion;
import com.othello.model.Tablero;
import com.othello.service.interfaces.IJugadorStrategy;
import org.springframework.stereotype.Component;

/**
 * Estrategia para jugador humano.
 * En este caso, el movimiento lo proporciona el usuario vía API.
 * Cumple con SRP: Solo implementa la interfaz para consistencia.
 * Cumple con LSP: Puede sustituir IJugadorStrategy sin problemas.
 */
@Component
public class JugadorHumanoStrategy implements IJugadorStrategy {

    @Override
    public Posicion calcularMovimiento(Tablero tablero, ColorFicha color) {
        // Para jugadores humanos, el movimiento se recibe por la API
        // Este método no se utiliza en la práctica para jugadores humanos
        return null;
    }
}
