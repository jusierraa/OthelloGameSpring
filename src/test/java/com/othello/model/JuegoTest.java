package com.othello.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JuegoTest {

    @Test
    void cambiarTurnoUsaElColorActualAunqueElJugadorSeaUnaInstanciaDeserializada() {
        Juego juego = new Juego(
            new Jugador("Black", ColorFicha.B, TipoJugador.HUMANO),
            new Jugador("White", ColorFicha.W, TipoJugador.IA)
        );

        juego.setTurnoActual(new Jugador("Black", ColorFicha.B, TipoJugador.HUMANO));
        juego.cambiarTurno();

        assertEquals(ColorFicha.W, juego.getTurnoActual().getColor());
    }

    @Test
    void getJugadorContrarioResuelvePorColorActual() {
        Juego juego = new Juego(
            new Jugador("Black", ColorFicha.B, TipoJugador.HUMANO),
            new Jugador("White", ColorFicha.W, TipoJugador.HUMANO)
        );

        juego.setTurnoActual(new Jugador("White", ColorFicha.W, TipoJugador.HUMANO));

        assertEquals(ColorFicha.B, juego.getJugadorContrario().getColor());
    }
}
