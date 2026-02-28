package com.othello.model;

/**
 * Enumeración que representa los colores de las fichas en el juego.
 */
public enum ColorFicha {
    B("B"),  // Black
    W("W"),  // White
    EMPTY("");

    private final String simbolo;

    ColorFicha(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public static ColorFicha fromSimbolo(String simbolo) {
        for (ColorFicha color : values()) {
            if (color.simbolo.equals(simbolo)) {
                return color;
            }
        }
        throw new IllegalArgumentException("Símbolo no válido: " + simbolo);
    }

    public ColorFicha contrario() {
        return switch (this) {
            case B -> W;
            case W -> B;
            case EMPTY -> EMPTY;
        };
    }
}
