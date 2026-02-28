package com.othello.dto;

import com.othello.model.ColorFicha;
import com.othello.model.TipoJugador;

/**
 * DTO para representar un jugador en las respuestas de la API.
 */
public class JugadorDTO {
    private String nombre;
    private ColorFicha color;
    private TipoJugador tipo;
    private int puntaje;

    public JugadorDTO() {
    }

    public JugadorDTO(String nombre, ColorFicha color, TipoJugador tipo, int puntaje) {
        this.nombre = nombre;
        this.color = color;
        this.tipo = tipo;
        this.puntaje = puntaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ColorFicha getColor() {
        return color;
    }

    public void setColor(ColorFicha color) {
        this.color = color;
    }

    public TipoJugador getTipo() {
        return tipo;
    }

    public void setTipo(TipoJugador tipo) {
        this.tipo = tipo;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
