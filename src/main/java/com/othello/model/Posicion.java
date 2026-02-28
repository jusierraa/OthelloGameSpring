package com.othello.model;

/**
 * Clase que representa una posición en el tablero.
 */
public class Posicion {
    private int row;
    private int col;

    public Posicion(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean esValida(int tamano) {
        return row >= 0 && row < tamano && col >= 0 && col < tamano;
    }
}
