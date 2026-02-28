package com.othello.dto;

import com.othello.model.ColorFicha;
import com.othello.model.EstadoJuego;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO para representar el estado completo del juego.
 */
public class JuegoDTO {
    private String id;
    private JugadorDTO jugador1;
    private JugadorDTO jugador2;
    private ColorFicha turnoActual;
    private TableroDTO tablero;
    private EstadoJuego estado;
    private Map<String, Integer> puntajes;
    private String mensajeEstado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimoMovimiento;
    private List<PosicionDTO> movimientosValidos;
    private Long version;
    private Integer passCount;
    private String winner;

    public JuegoDTO() {
    }

    public JuegoDTO(String id, JugadorDTO jugador1, JugadorDTO jugador2, ColorFicha turnoActual,
                    TableroDTO tablero, EstadoJuego estado, Map<String, Integer> puntajes,
                    String mensajeEstado, LocalDateTime fechaCreacion, LocalDateTime fechaUltimoMovimiento,
                    List<PosicionDTO> movimientosValidos, Long version, Integer passCount, String winner) {
        this.id = id;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.turnoActual = turnoActual;
        this.tablero = tablero;
        this.estado = estado;
        this.puntajes = puntajes;
        this.mensajeEstado = mensajeEstado;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimoMovimiento = fechaUltimoMovimiento;
        this.movimientosValidos = movimientosValidos;
        this.version = version;
        this.passCount = passCount;
        this.winner = winner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JugadorDTO getJugador1() {
        return jugador1;
    }

    public void setJugador1(JugadorDTO jugador1) {
        this.jugador1 = jugador1;
    }

    public JugadorDTO getJugador2() {
        return jugador2;
    }

    public void setJugador2(JugadorDTO jugador2) {
        this.jugador2 = jugador2;
    }

    public ColorFicha getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(ColorFicha turnoActual) {
        this.turnoActual = turnoActual;
    }

    public TableroDTO getTablero() {
        return tablero;
    }

    public void setTablero(TableroDTO tablero) {
        this.tablero = tablero;
    }

    public EstadoJuego getEstado() {
        return estado;
    }

    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
    }

    public Map<String, Integer> getPuntajes() {
        return puntajes;
    }

    public void setPuntajes(Map<String, Integer> puntajes) {
        this.puntajes = puntajes;
    }

    public String getMensajeEstado() {
        return mensajeEstado;
    }

    public void setMensajeEstado(String mensajeEstado) {
        this.mensajeEstado = mensajeEstado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaUltimoMovimiento() {
        return fechaUltimoMovimiento;
    }

    public void setFechaUltimoMovimiento(LocalDateTime fechaUltimoMovimiento) {
        this.fechaUltimoMovimiento = fechaUltimoMovimiento;
    }

    public List<PosicionDTO> getMovimientosValidos() {
        return movimientosValidos;
    }

    public void setMovimientosValidos(List<PosicionDTO> movimientosValidos) {
        this.movimientosValidos = movimientosValidos;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getPassCount() {
        return passCount;
    }

    public void setPassCount(Integer passCount) {
        this.passCount = passCount;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
