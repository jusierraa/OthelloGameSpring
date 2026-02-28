package com.othello.model;

/**
 * Clase que representa el tablero del juego de Othello.
 * Cumple con SRP: Solo representa el estado del tablero y proporciona acceso a las fichas.
 */
public class Tablero {
    private final int tamano;
    private Ficha[][] matriz;

    public Tablero(int tamano) {
        this.tamano = tamano;
        this.matriz = new Ficha[tamano][tamano];
        inicializarTablero();
    }

    public int getTamano() {
        return tamano;
    }

    public Ficha[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(Ficha[][] matriz) {
        this.matriz = matriz;
    }

    /**
     * Inicializa el tablero con las fichas en su posición inicial.
     */
    private void inicializarTablero() {
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                matriz[i][j] = new Ficha();
            }
        }
        
        // Configuración inicial del Othello (4 fichas centrales)
        int centro = tamano / 2;
        matriz[centro - 1][centro - 1].setColor(ColorFicha.W);
        matriz[centro][centro].setColor(ColorFicha.W);
        matriz[centro - 1][centro].setColor(ColorFicha.B);
        matriz[centro][centro - 1].setColor(ColorFicha.B);
    }

    /**
     * Obtiene una ficha en una posición específica.
     */
    public Ficha getFicha(int fila, int columna) {
        if (fila >= 0 && fila < tamano && columna >= 0 && columna < tamano) {
            return matriz[fila][columna];
        }
        return null;
    }

    /**
     * Establece una ficha en una posición específica.
     */
    public void setFicha(int fila, int columna, ColorFicha color) {
        if (fila >= 0 && fila < tamano && columna >= 0 && columna < tamano) {
            matriz[fila][columna].setColor(color);
        }
    }

    /**
     * Verifica si hay alguna casilla vacía en el tablero.
     */
    public boolean tieneEspaciosVacios() {
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                if (matriz[i][j].estaVacia()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Cuenta las fichas de un color específico.
     */
    public int contarFichas(ColorFicha color) {
        int contador = 0;
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                if (matriz[i][j].getColor() == color) {
                    contador++;
                }
            }
        }
        return contador;
    }

    /**
     * Crea una copia del tablero.
     */
    public Tablero copiar() {
        Tablero copia = new Tablero(this.tamano);
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                copia.matriz[i][j].setColor(this.matriz[i][j].getColor());
            }
        }
        return copia;
    }
}
