package com.othello.dto;

/**
 * DTO para representar el tablero en las respuestas de la API.
 */
public class TableroDTO {
    private String[][] board;
    private int tamano;

    public TableroDTO() {
    }

    public TableroDTO(String[][] board, int tamano) {
        this.board = board;
        this.tamano = tamano;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }
}
