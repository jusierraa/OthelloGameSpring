package com.othello.service.impl;

import com.othello.dto.*;
import com.othello.exception.JuegoNoEncontradoException;
import com.othello.exception.MovimientoInvalidoException;
import com.othello.model.*;
import com.othello.repository.JuegoRepository;
import com.othello.repository.UsuarioRepository;
import com.othello.service.interfaces.IJuegoService;
import com.othello.service.interfaces.IJugadorStrategy;
import com.othello.service.interfaces.ITableroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
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
    private final UsuarioRepository usuarioRepository;
    private final ITableroService tableroService;
    private final IJugadorStrategy jugadorIAStrategy;

    public JuegoServiceImpl(JuegoRepository juegoRepository, 
                           UsuarioRepository usuarioRepository,
                           ITableroService tableroService,
                           JugadorIAStrategy jugadorIAStrategy) {
        this.juegoRepository = juegoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tableroService = tableroService;
        this.jugadorIAStrategy = jugadorIAStrategy;
    }

    @Override
    public JuegoDTO crearJuego(CrearJuegoDTO crearJuegoDTO, String ownerUsername) {
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
        juego.setOwnerUsername(ownerUsername);
        juego.actualizarPuntajes();

        // Guardar en repositorio
        juegoRepository.save(juego);

        logger.info("Juego creado con ID: {}", juego.getId());
        return convertirADTO(juego);
    }

    @Override
    public JuegoDTO obtenerJuego(String juegoId, String ownerUsername) {
        Juego juego = obtenerJuegoAutorizado(juegoId, ownerUsername);
        return convertirADTO(juego);
    }

    @Override
    public JuegoDTO realizarMovimiento(String juegoId, MovimientoDTO movimiento, String ownerUsername) {
        Juego juego = obtenerJuegoAutorizado(juegoId, ownerUsername);

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
        juego.setMensajeEstado("Turn: " + obtenerEtiquetaColor(juego.getJugadorContrario().getColor()) + ".");

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
                juego.setMensajeEstado(
                    obtenerEtiquetaColor(juego.getJugadorContrario().getColor()) +
                    " has no valid moves. " +
                    obtenerEtiquetaColor(juego.getTurnoActual().getColor()) +
                    " plays again."
                );
            }
        } else {
            juego.setMensajeEstado("Turn: " + obtenerEtiquetaColor(juego.getTurnoActual().getColor()) + ".");
        }

        // Si es turno de la IA y el juego sigue en curso, procesar movimiento de IA
        if (juego.getEstado() == EstadoJuego.ACTIVE && 
            juego.getTurnoActual().getTipo() == TipoJugador.IA) {
            procesarMovimientoIA(juego);
        }

        registrarResultadoSiAplica(juego);

        // Guardar cambios
        juegoRepository.save(juego);

        return convertirADTO(juego);
    }

    @Override
    public List<JuegoDTO> obtenerTodosLosJuegos(String ownerUsername) {
        return juegoRepository.findByOwnerUsernameOrderByFechaUltimoMovimientoDesc(ownerUsername).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    @Override
    public void eliminarJuego(String juegoId, String ownerUsername) {
        Juego juego = obtenerJuegoAutorizado(juegoId, ownerUsername);
        juegoRepository.deleteById(juego.getId());
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
        String nombreIA = juego.getTurnoActual().getNombre();
        Posicion movimientoIA = jugadorIAStrategy.calcularMovimiento(
            juego.getTablero(), 
            juego.getTurnoActual().getColor()
        );

        if (movimientoIA == null) {
            juego.cambiarTurno();
            juego.incrementarPassCount();

            if (!tableroService.tieneMovimientosValidos(juego.getTablero(), juego.getTurnoActual().getColor())) {
                juego.finalizarJuego();
            } else {
                juego.setMensajeEstado(nombreIA + " has no valid moves. Turn: " +
                    obtenerEtiquetaColor(juego.getTurnoActual().getColor()) + ".");
            }
            return;
        }

        logger.info("IA realizando movimiento: [{},{}]", 
                   movimientoIA.getRow(), movimientoIA.getCol());

        tableroService.ejecutarMovimiento(
            juego.getTablero(), 
            movimientoIA.getRow(), 
            movimientoIA.getCol(), 
            juego.getTurnoActual().getColor()
        );

        juego.actualizarPuntajes();
        juego.resetPassCount();
        juego.cambiarTurno();

        // Verificar si el siguiente jugador tiene movimientos
        if (!tableroService.tieneMovimientosValidos(
                juego.getTablero(), 
                juego.getTurnoActual().getColor())) {
            juego.cambiarTurno();
            juego.incrementarPassCount();

            if (!tableroService.tieneMovimientosValidos(
                    juego.getTablero(), 
                    juego.getTurnoActual().getColor())) {
                juego.finalizarJuego();
            } else {
                juego.setMensajeEstado(
                    obtenerEtiquetaColor(juego.getJugadorContrario().getColor()) +
                    " has no valid moves. " +
                    obtenerEtiquetaColor(juego.getTurnoActual().getColor()) +
                    " plays again."
                );
            }
        } else {
            juego.setMensajeEstado(nombreIA + " moved. Turn: " +
                obtenerEtiquetaColor(juego.getTurnoActual().getColor()) + ".");
        }
    }

    private Juego obtenerJuegoAutorizado(String juegoId, String ownerUsername) {
        Juego juego = juegoRepository.findById(juegoId)
            .orElseThrow(() -> new JuegoNoEncontradoException("Juego no encontrado con ID: " + juegoId));

        if (juego.getOwnerUsername() == null || !juego.getOwnerUsername().equals(ownerUsername)) {
            throw new AccessDeniedException("No tienes permiso para acceder a este juego");
        }

        return juego;
    }

    private void registrarResultadoSiAplica(Juego juego) {
        if (juego.getEstado() == EstadoJuego.ACTIVE || juego.getJugador2().getTipo() != TipoJugador.IA) {
            return;
        }

        Usuario usuario = usuarioRepository.findByUsername(juego.getOwnerUsername())
            .orElseThrow(() -> new AccessDeniedException("Usuario propietario no encontrado"));

        if (juego.getEstado() == EstadoJuego.DRAW) {
            usuario.registrarEmpate();
        } else if (juego.getWinner() != null && juego.getWinner().equals(juego.getJugador1().getNombre())) {
            usuario.registrarVictoria();
        } else {
            usuario.registrarDerrota();
        }

        usuarioRepository.save(usuario);
    }

    private String obtenerEtiquetaColor(ColorFicha color) {
        return color == ColorFicha.B ? "Black" : "White";
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
