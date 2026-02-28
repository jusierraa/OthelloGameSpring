package com.othello.model;

/**
 * Clase que representa un jugador en el juego de Othello.
 * Cumple con SRP: Solo almacena información del jugador.
 */
public class Jugador {
    private String nombre;
    private ColorFicha color;
    private TipoJugador tipo;
    private int puntaje;

    public Jugador(String nombre, ColorFicha color, TipoJugador tipo) {
        this.nombre = nombre;
        this.color = color;
        this.tipo = tipo;
        this.puntaje = 0;
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

    public void incrementarPuntaje() {
        this.puntaje++;
    }
}
