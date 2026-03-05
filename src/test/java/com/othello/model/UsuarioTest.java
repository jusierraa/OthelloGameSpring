package com.othello.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioTest {

    @Test
    void registrarVictoriaIncrementaPartidasJugadasYGanadas() {
        Usuario usuario = new Usuario();

        usuario.registrarVictoria();

        assertEquals(1, usuario.getPartidasJugadas());
        assertEquals(1, usuario.getPartidasGanadas());
        assertEquals(0, usuario.getPartidasPerdidas());
        assertEquals(0, usuario.getPartidasEmpatadas());
    }

    @Test
    void registrarEmpateIncrementaPartidasJugadasYEmpatadas() {
        Usuario usuario = new Usuario();

        usuario.registrarEmpate();

        assertEquals(1, usuario.getPartidasJugadas());
        assertEquals(1, usuario.getPartidasEmpatadas());
        assertEquals(0, usuario.getPartidasGanadas());
        assertEquals(0, usuario.getPartidasPerdidas());
    }
}
