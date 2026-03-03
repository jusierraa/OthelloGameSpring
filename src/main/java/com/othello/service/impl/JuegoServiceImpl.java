package com.othello.service.impl;

import com.othello.dto.*;
import com.othello.exception.JuegoNoEncontradoException;
import com.othello.exception.MovimientoInvalidoException;
import com.othello.model.*;
import com.othello.repository.JuegoRepository;
import com.othello.service.interfaces.IJuegoService;
import com.othello.service.interfaces.IJugadorStrategy;
import com.othello.service.interfaces.ITableroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de juego.
 * Cumple con SRP: Solo orquesta la lógica del juego usando otros servicios.
 * Cumple con DIP: Depende de interfaces (ITableroService, IJugadorStrategy).
 */
@Service
public class JuegoServiceImpl implements IJuegoService {

    private static final Logger logger = LoggerFactory.getLogger(JuegoServiceImpl.class);

    private final JuegoRepository juegoRepository;
    private final ITableroService tableroService;
    private final IJugadorStrategy jugadorIAStrategy;

    public JuegoServiceImpl(JuegoRepository juegoRepository, 
                           ITableroService tableroService,
                           JugadorIAStrategy jugadorIAStrategy) {
        this.juegoRepository = juegoRepository;
        this.tableroService = tableroService;
        this.jugadorIAStrategy = jugadorIAStrategy;
    }

    @Override
    public JuegoDTO crearJuego(CrearJuegoDTO crearJuegoDTO) {
        logger.info("Creando nuevo juego: {} vs {}", 
                   crearJuegoDTO.getNombreJugador1(), 
                   crearJuegoDTO.getNombreJugador2());

        // Crear jugadores
        Jugador jugador1 = new Jugador(
            crearJuegoDTO.getNombreJugador1(), 
            ColorFicha.B, 
            TipoJugador.HUMANO
        );
        
        Jugador jugador2 = new Jugador(
            crearJuegoDTO.getNombreJugador2(), 
            ColorFicha.W, 
            crearJuegoDTO.getTipoJugador2()
        );

        // Crear juego
        Juego juego = new Juego(jugador1, jugador2);
        juego.actualizarPuntajes();

        // Guardar en repositorio
        juegoRepository.save(juego);

        logger.info("Juego creado con ID: {}", juego.getId());
        return convertirADTO(juego);
    }

    @Override
    public JuegoDTO obtenerJuego(String juegoId) {
        Juego juego = juegoRepository.findById(juegoId)
            .orElseThrow(() -> new JuegoNoEncontradoException("Juego no encontrado con ID: " + juegoId));
        return convertirADTO(juego);
    }

    @Override
    public JuegoDTO realizarMovimiento(String juegoId, MovimientoDTO movimiento) {
        Juego juego = juegoRepository.findById(juegoId)
            .orElseThrow(() -> new JuegoNoEncontradoException("Juego no encontrado con ID: " + juegoId));

        // Validar versión (concurrencia optimista)
        if (!juego.getVersion().equals(movimiento.getExpectedVersion())) {
            throw new com.othello.exception.VersionConflictException(
                movimiento.getExpectedVersion(), 
                juego.getVersion()
            );
        }

        // Verificar que el juego no haya terminado
        if (juego.getEstado() != EstadoJuego.ACTIVE) {
            throw new MovimientoInvalidoException("El juego ya ha finalizado");
        }

        ColorFicha colorActual = juego.getTurnoActual().getColor();

        // Validar el movimiento
        if (!tableroService.esMovimientoValido(
                juego.getTablero(), 
                movimiento.getRow(), 
                movimiento.getCol(), 
                colorActual)) {
            throw new MovimientoInvalidoException(
                "Movimiento inválido en posición [" + movimiento.getRow() + "," + movimiento.getCol() + "]"
            );
        }

        // Ejecutar el movimiento
        tableroService.ejecutarMovimiento(
            juego.getTablero(), 
            movimiento.getRow(), 
            movimiento.getCol(), 
            colorActual
        );

        logger.info("Movimiento realizado en juego {}: [{},{}]", 
                   juegoId, movimiento.getRow(), movimiento.getCol());

        // Actualizar puntajes y reset passCount ya que hubo movimiento válido
        juego.actualizarPuntajes();
        juego.resetPassCount();

        // Verificar si el siguiente jugador tiene movimientos
        juego.cambiarTurno();
        if (!tableroService.tieneMovimientosValidos(juego.getTablero(), juego.getTurnoActual().getColor())) {
            // El siguiente jugador no tiene movimientos, volver al anterior
            juego.cambiarTurno();
            juego.incrementarPassCount();
            
            if (!tableroService.tieneMovimientosValidos(juego.getTablero(), juego.getTurnoActual().getColor())) {
                // Ningún jugador tiene movimientos, finalizar juego
                juego.finalizarJuego();
                logger.info("Juego {} finalizado: {}", juegoId, juego.getMensajeEstado());
            } else {
                juego.setMensajeEstado("El oponente no tiene movimientos válidos. Turno repetido.");
            }
        }

        // Si es turno de la IA y el juego sigue en curso, procesar movimiento de IA
        if (juego.getEstado() == EstadoJuego.ACTIVE && 
            juego.getTurnoActual().getTipo() == TipoJugador.IA) {
            procesarMovimientoIA(juego);
        }

        // Guardar cambios
        juegoRepository.save(juego);

        return convertirADTO(juego);
    }

    @Override
    public List<JuegoDTO> obtenerTodosLosJuegos() {
        return juegoRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    @Override
    public void eliminarJuego(String juegoId) {
        if (!juegoRepository.existsById(juegoId)) {
            throw new JuegoNoEncontradoException("Juego no encontrado con ID: " + juegoId);
        }
        juegoRepository.deleteById(juegoId);
        logger.info("Juego eliminado: {}", juegoId);
    }

    @Override
    public JuegoDTO convertirADTO(Juego juego) {
        JuegoDTO dto = new JuegoDTO();
        dto.setId(juego.getId());
        dto.setJugador1(convertirJugadorADTO(juego.getJugador1()));
        dto.setJugador2(convertirJugadorADTO(juego.getJugador2()));
        dto.setTurnoActual(juego.getTurnoActual().getColor());
        dto.setTablero(convertirTableroADTO(juego.getTablero()));
        dto.setEstado(juego.getEstado());
        
        Map<String, Integer> puntajes = new HashMap<>();
        puntajes.put(juego.getJugador1().getNombre(), juego.getJugador1().getPuntaje());
        puntajes.put(juego.getJugador2().getNombre(), juego.getJugador2().getPuntaje());
        dto.setPuntajes(puntajes);
        
        dto.setMensajeEstado(juego.getMensajeEstado());
        dto.setFechaCreacion(juego.getFechaCreacion());
        dto.setFechaUltimoMovimiento(juego.getFechaUltimoMovimiento());
        dto.setVersion(juego.getVersion());
        dto.setPassCount(juego.getPassCount());
        dto.setWinner(juego.getWinner());
        
        // Agregar movimientos válidos si el juego está en curso
        if (juego.getEstado() == EstadoJuego.ACTIVE) {
            List<Posicion> movimientosValidos = tableroService.obtenerMovimientosValidos(
                juego.getTablero(), 
                juego.getTurnoActual().getColor()
            );
            dto.setMovimientosValidos(
                movimientosValidos.stream()
                    .map(p -> new PosicionDTO(p.getRow(), p.getCol()))
                    .collect(Collectors.toList())
            );
        }
        
        return dto;
    }

    /**
     * Procesa el movimiento automático de la IA.
     */
    private void procesarMovimientoIA(Juego juego) {
        Posicion movimientoIA = jugadorIAStrategy.calcularMovimiento(
            juego.getTablero(), 
            juego.getTurnoActual().getColor()
        );

        if (movimientoIA != null) {
            logger.info("IA realizando movimiento: [{},{}]", 
                       movimientoIA.getRow(), movimientoIA.getCol());

            tableroService.ejecutarMovimiento(
                juego.getTablero(), 
                movimientoIA.getRow(), 
                movimientoIA.getCol(), 
                juego.getTurnoActual().getColor()
            );

            juego.actualizarPuntajes();
            juego.cambiarTurno();

            // Verificar si el siguiente jugador tiene movimientos
            if (!tableroService.tieneMovimientosValidos(
                    juego.getTablero(), 
                    juego.getTurnoActual().getColor())) {
                juego.cambiarTurno();
                
                if (!tableroService.tieneMovimientosValidos(
                        juego.getTablero(), 
                        juego.getTurnoActual().getColor())) {
                    juego.finalizarJuego();
                }
            }
        }
    }

    private JugadorDTO convertirJugadorADTO(Jugador jugador) {
        return new JugadorDTO(
            jugador.getNombre(),
            jugador.getColor(),
            jugador.getTipo(),
            jugador.getPuntaje()
        );
    }

    private TableroDTO convertirTableroADTO(Tablero tablero) {
        String[][] matriz = new String[tablero.getTamano()][tablero.getTamano()];
        for (int i = 0; i < tablero.getTamano(); i++) {
            for (int j = 0; j < tablero.getTamano(); j++) {
                matriz[i][j] = tablero.getFicha(i, j).getColor().getSimbolo();
            }
        }
        return new TableroDTO(matriz, tablero.getTamano());
    }
}
