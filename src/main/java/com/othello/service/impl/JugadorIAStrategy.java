package com.othello.service.impl;

import com.othello.model.ColorFicha;
import com.othello.model.Posicion;
import com.othello.model.Tablero;
import com.othello.service.interfaces.IJugadorStrategy;
import com.othello.service.interfaces.ITableroService;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Estrategia para jugador controlado por IA.
 * Cumple con SRP: Solo implementa la lógica de decisión de la IA.
 * Cumple con LSP: Puede sustituir IJugadorStrategy sin problemas.
 */
@Component
public class JugadorIAStrategy implements IJugadorStrategy {

    private final ITableroService tableroService;

    public JugadorIAStrategy(ITableroService tableroService) {
        this.tableroService = tableroService;
    }

    @Override
    public Posicion calcularMovimiento(Tablero tablero, ColorFicha color) {
        List<Posicion> movimientosValidos = tableroService.obtenerMovimientosValidos(tablero, color);
        
        if (movimientosValidos.isEmpty()) {
            return null;
        }

        // Estrategia: elegir el movimiento que voltee más fichas
        Posicion mejorMovimiento = null;
        int maxFichasVolteadas = 0;

        for (Posicion pos : movimientosValidos) {
            int fichasVolteadas = contarFichasVolteadas(tablero, pos.getRow(), pos.getCol(), color);
            if (fichasVolteadas > maxFichasVolteadas) {
                maxFichasVolteadas = fichasVolteadas;
                mejorMovimiento = pos;
            }
        }

        return mejorMovimiento;
    }

    /**
     * Cuenta cuántas fichas se voltearían con un movimiento específico.
     */
    private int contarFichasVolteadas(Tablero tableroOriginal, int fila, int columna, ColorFicha color) {
        // Crear una copia del tablero para simular el movimiento
        Tablero tableroSimulado = tableroOriginal.copiar();
        
        int fichasAntes = tableroSimulado.contarFichas(color);
        tableroService.ejecutarMovimiento(tableroSimulado, fila, columna, color);
        int fichasDespues = tableroSimulado.contarFichas(color);
        
        // La diferencia menos 1 (la ficha que colocamos) es el número de fichas volteadas
        return fichasDespues - fichasAntes - 1;
    }
}
