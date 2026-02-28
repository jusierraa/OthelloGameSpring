package com.othello.dto;

import com.othello.model.TipoJugador;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para crear un nuevo juego.
 */
public class CrearJuegoDTO {
    
    @NotBlank(message = "El nombre del jugador 1 no puede estar vacío")
    private String nombreJugador1;
    
    @NotBlank(message = "El nombre del jugador 2 no puede estar vacío")
    private String nombreJugador2;
    
    @NotNull(message = "El tipo de jugador 2 no puede ser nulo")
    private TipoJugador tipoJugador2;

    public CrearJuegoDTO() {
    }

    public CrearJuegoDTO(String nombreJugador1, String nombreJugador2, TipoJugador tipoJugador2) {
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;
        this.tipoJugador2 = tipoJugador2;
    }

    public String getNombreJugador1() {
        return nombreJugador1;
    }

    public void setNombreJugador1(String nombreJugador1) {
        this.nombreJugador1 = nombreJugador1;
    }

    public String getNombreJugador2() {
        return nombreJugador2;
    }

    public void setNombreJugador2(String nombreJugador2) {
        this.nombreJugador2 = nombreJugador2;
    }

    public TipoJugador getTipoJugador2() {
        return tipoJugador2;
    }

    public void setTipoJugador2(TipoJugador tipoJugador2) {
        this.tipoJugador2 = tipoJugador2;
    }
}
