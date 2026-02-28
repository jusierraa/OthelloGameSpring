package com.othello.model;

/**
 * Clase que representa una ficha en el tablero de Othello.
 * Cumple con SRP: Solo representa el estado de una posición del tablero.
 */
public class Ficha {
    private ColorFicha color;

    public Ficha() {
        this.color = ColorFicha.EMPTY;
    }

    public Ficha(ColorFicha color) {
        this.color = color;
    }

    public ColorFicha getColor() {
        return color;
    }

    public void setColor(ColorFicha color) {
        this.color = color;
    }

    public boolean estaVacia() {
        return color == ColorFicha.EMPTY;
    }

    public void voltear() {
        if (color != ColorFicha.EMPTY) {
            color = color.contrario();
        }
    }
}
