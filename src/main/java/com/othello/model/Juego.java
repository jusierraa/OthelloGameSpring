package com.othello.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase que representa una partida de Othello.
 * Cumple con SRP: Solo almacena el estado de una partida.
 */
public class Juego {
    private String id;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador turnoActual;
    private Tablero tablero;
    private EstadoJuego estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimoMovimiento;
    private String mensajeEstado;
    private Long version;
    private Integer passCount;
    private String winner;

    public Juego(Jugador jugador1, Jugador jugador2) {
        this.id = UUID.randomUUID().toString();
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.turnoActual = jugador1;
        this.tablero = new Tablero(8);
        this.estado = EstadoJuego.ACTIVE;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaUltimoMovimiento = LocalDateTime.now();
        this.mensajeEstado = "Juego iniciado";
        this.version = 0L;
        this.passCount = 0;
        this.winner = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    public Jugador getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(Jugador turnoActual) {
        this.turnoActual = turnoActual;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public EstadoJuego getEstado() {
        return estado;
    }

    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
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

    public String getMensajeEstado() {
        return mensajeEstado;
    }

    public void setMensajeEstado(String mensajeEstado) {
        this.mensajeEstado = mensajeEstado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void incrementarVersion() {
        this.version++;
    }

    public Integer getPassCount() {
        return passCount;
    }

    public void setPassCount(Integer passCount) {
        this.passCount = passCount;
    }

    public void incrementarPassCount() {
        this.passCount++;
    }

    public void resetPassCount() {
        this.passCount = 0;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Cambia el turno al siguiente jugador.
     */
    public void cambiarTurno() {
        this.turnoActual = (turnoActual == jugador1) ? jugador2 : jugador1;
        this.fechaUltimoMovimiento = LocalDateTime.now();
        this.incrementarVersion();
    }

    /**
     * Obtiene el jugador contrario al turno actual.
     */
    public Jugador getJugadorContrario() {
        return (turnoActual == jugador1) ? jugador2 : jugador1;
    }

    /**
     * Actualiza los puntajes basándose en el estado del tablero.
     */
    public void actualizarPuntajes() {
        int puntajeJ1 = tablero.contarFichas(jugador1.getColor());
        int puntajeJ2 = tablero.contarFichas(jugador2.getColor());
        jugador1.setPuntaje(puntajeJ1);
        jugador2.setPuntaje(puntajeJ2);
    }

    /**
     * Finaliza el juego y determina el ganador.
     */
    public void finalizarJuego() {
        actualizarPuntajes();
        if (jugador1.getPuntaje() > jugador2.getPuntaje()) {
            this.estado = EstadoJuego.FINISHED;
            this.winner = jugador1.getNombre();
            this.mensajeEstado = "¡" + jugador1.getNombre() + " ha ganado con " + 
                                 jugador1.getPuntaje() + " fichas!";
        } else if (jugador2.getPuntaje() > jugador1.getPuntaje()) {
            this.estado = EstadoJuego.FINISHED;
            this.winner = jugador2.getNombre();
            this.mensajeEstado = "¡" + jugador2.getNombre() + " ha ganado con " + 
                                 jugador2.getPuntaje() + " fichas!";
        } else {
            this.estado = EstadoJuego.DRAW;
            this.winner = null;
            this.mensajeEstado = "¡Empate! Ambos jugadores tienen " + 
                                 jugador1.getPuntaje() + " fichas";
        }
    }
}
