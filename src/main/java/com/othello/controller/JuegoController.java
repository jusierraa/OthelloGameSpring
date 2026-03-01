package com.othello.controller;

import com.othello.dto.*;
import com.othello.service.interfaces.IJuegoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para el juego de Othello.
 * Cumple con SRP: Solo maneja las peticiones HTTP y delega la lógica al servicio.
 * 
 * @author Grupo 6
 */
@RestController
@RequestMapping("/juego")
@CrossOrigin(origins = "*")
public class JuegoController {

    private static final Logger logger = LoggerFactory.getLogger(JuegoController.class);

    private final IJuegoService juegoService;

    public JuegoController(IJuegoService juegoService) {
        this.juegoService = juegoService;
    }

    /**
     * Crear una nueva partida.
     * POST /api/juego/nuevo
     */
    @PostMapping("/nuevo")
    public ResponseEntity<GameResponseDTO> crearJuego(@Valid @RequestBody CrearJuegoDTO crearJuegoDTO) {
        logger.info("Solicitud para crear nuevo juego");
        JuegoDTO juego = juegoService.crearJuego(crearJuegoDTO);
        GameResponseDTO respuesta = convertirAGameResponse(juego);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    /**
     * Obtener el estado de un juego.
     * GET /api/juego/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> obtenerJuego(@PathVariable String id) {
        logger.info("Solicitud para obtener juego: {}", id);
        JuegoDTO juego = juegoService.obtenerJuego(id);
        GameResponseDTO respuesta = convertirAGameResponse(juego);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Realizar un movimiento en el juego.
     * POST /api/juego/{id}/movimiento
     */
    @PostMapping("/{id}/movimiento")
    public ResponseEntity<GameResponseDTO> realizarMovimiento(
            @PathVariable String id, 
            @Valid @RequestBody MovimientoDTO movimiento) {
        logger.info("Solicitud de movimiento en juego {}: [{},{}]", 
                   id, movimiento.getRow(), movimiento.getCol());
        JuegoDTO juego = juegoService.realizarMovimiento(id, movimiento);
        GameResponseDTO respuesta = convertirAGameResponse(juego);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Obtener todos los juegos activos.
     * GET /api/juego/todos
     */
    @GetMapping("/todos")
    public ResponseEntity<List<GameResponseDTO>> obtenerTodosLosJuegos() {
        logger.info("Solicitud para obtener todos los juegos");
        List<JuegoDTO> juegos = juegoService.obtenerTodosLosJuegos();
        List<GameResponseDTO> respuestas = juegos.stream()
                .map(this::convertirAGameResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    /**
     * Eliminar un juego.
     * DELETE /api/juego/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarJuego(@PathVariable String id) {
        logger.info("Solicitud para eliminar juego: {}", id);
        juegoService.eliminarJuego(id);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("message", "Juego eliminado exitosamente");
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Endpoint de salud para verificar que el servicio está funcionando.
     * GET /api/juego/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("status", "ok");
        respuesta.put("message", "Othello Backend API está funcionando correctamente");
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Convierte un JuegoDTO al formato GameResponseDTO { "game": {...} }.
     */
    private GameResponseDTO convertirAGameResponse(JuegoDTO juegoDTO) {
        GameResponseDTO.GameDTO gameDTO = new GameResponseDTO.GameDTO();
        gameDTO.setId(juegoDTO.getId());
        gameDTO.setBoard(juegoDTO.getTablero().getBoard());
        gameDTO.setCurrentPlayer(juegoDTO.getTurnoActual().name());
        gameDTO.setStatus(mapearEstado(juegoDTO.getEstado()));
        gameDTO.setPassCount(juegoDTO.getPassCount());
        gameDTO.setWinner(juegoDTO.getWinner());
        gameDTO.setScore(juegoDTO.getPuntajes());
        gameDTO.setValidMoves(juegoDTO.getMovimientosValidos());
        gameDTO.setVersion(juegoDTO.getVersion());
        
        return new GameResponseDTO(gameDTO);
    }
    
    /**
     * Mapea EstadoJuego a string en minúsculas para la API.
     */
    private String mapearEstado(com.othello.model.EstadoJuego estado) {
        return switch (estado) {
            case ACTIVE -> "active";
            case FINISHED -> "finished";
            case DRAW -> "draw";
        };
    }
}
