package com.othello.service.impl;

import com.othello.model.ColorFicha;
import com.othello.model.Posicion;
import com.othello.model.Tablero;
import com.othello.service.interfaces.ITableroService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de tablero.
 * Cumple con SRP: Solo maneja la lógica relacionada con el tablero.
 */
@Service
public class TableroServiceImpl implements ITableroService {

    private static final int[][] DIRECCIONES = {
        {-1, 0},  // Arriba
        {1, 0},   // Abajo
        {0, -1},  // Izquierda
        {0, 1},   // Derecha
        {-1, -1}, // Arriba-Izquierda
        {-1, 1},  // Arriba-Derecha
        {1, -1},  // Abajo-Izquierda
        {1, 1}    // Abajo-Derecha
    };

    @Override
    public boolean esMovimientoValido(Tablero tablero, int fila, int columna, ColorFicha color) {
        // Verificar que la casilla esté vacía
        if (!tablero.getFicha(fila, columna).estaVacia()) {
            return false;
        }

        // Verificar que al menos en una dirección se puedan voltear fichas
        for (int[] direccion : DIRECCIONES) {
            if (verificarDireccion(tablero, fila, columna, direccion[0], direccion[1], color)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void ejecutarMovimiento(Tablero tablero, int fila, int columna, ColorFicha color) {
        // Colocar la ficha
        tablero.setFicha(fila, columna, color);

        // Voltear fichas en todas las direcciones válidas
        for (int[] direccion : DIRECCIONES) {
            if (verificarDireccion(tablero, fila, columna, direccion[0], direccion[1], color)) {
                voltearFichasEnDireccion(tablero, fila, columna, direccion[0], direccion[1], color);
            }
        }
    }

    @Override
    public List<Posicion> obtenerMovimientosValidos(Tablero tablero, ColorFicha color) {
        List<Posicion> movimientos = new ArrayList<>();
        for (int i = 0; i < tablero.getTamano(); i++) {
            for (int j = 0; j < tablero.getTamano(); j++) {
                if (esMovimientoValido(tablero, i, j, color)) {
                    movimientos.add(new Posicion(i, j));
                }
            }
        }
        return movimientos;
    }

    @Override
    public boolean tieneMovimientosValidos(Tablero tablero, ColorFicha color) {
        return !obtenerMovimientosValidos(tablero, color).isEmpty();
    }

    /**
     * Verifica si se pueden voltear fichas en una dirección específica.
     */
    private boolean verificarDireccion(Tablero tablero, int fila, int columna, 
                                      int deltaFila, int deltaColumna, ColorFicha color) {
        int filActual = fila + deltaFila;
        int colActual = columna + deltaColumna;
        boolean hayFichasContrarias = false;

        // Recorrer en la dirección especificada
        while (filActual >= 0 && filActual < tablero.getTamano() && 
               colActual >= 0 && colActual < tablero.getTamano()) {
            
            ColorFicha colorActual = tablero.getFicha(filActual, colActual).getColor();

            // Si encuentra una casilla vacía, no es válido
            if (colorActual == ColorFicha.EMPTY) {
                return false;
            }

            // Si encuentra una ficha del mismo color
            if (colorActual == color) {
                // Solo es válido si había fichas contrarias en el medio
                return hayFichasContrarias;
            }

            // Es una ficha contraria
            hayFichasContrarias = true;
            filActual += deltaFila;
            colActual += deltaColumna;
        }

        return false;
    }

    /**
     * Voltea las fichas en una dirección específica.
     */
    private void voltearFichasEnDireccion(Tablero tablero, int fila, int columna, 
                                         int deltaFila, int deltaColumna, ColorFicha color) {
        int filActual = fila + deltaFila;
        int colActual = columna + deltaColumna;

        while (filActual >= 0 && filActual < tablero.getTamano() && 
               colActual >= 0 && colActual < tablero.getTamano()) {
            
            ColorFicha colorActual = tablero.getFicha(filActual, colActual).getColor();

            // Si encuentra una ficha del mismo color, detener
            if (colorActual == color) {
                break;
            }

            // Voltear la ficha
            tablero.setFicha(filActual, colActual, color);

            filActual += deltaFila;
            colActual += deltaColumna;
        }
    }
}
