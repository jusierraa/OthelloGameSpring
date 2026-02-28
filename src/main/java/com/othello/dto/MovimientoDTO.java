package com.othello.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para realizar un movimiento en el juego.
 */
public class MovimientoDTO {
    
    @NotNull(message = "La fila no puede ser nula")
    @Min(value = 0, message = "La fila debe ser mayor o igual a 0")
    @Max(value = 7, message = "La fila debe ser menor o igual a 7")
    private Integer row;
    
    @NotNull(message = "La columna no puede ser nula")
    @Min(value = 0, message = "La columna debe ser mayor o igual a 0")
    @Max(value = 7, message = "La columna debe ser menor o igual a 7")
    private Integer col;
    
    @NotNull(message = "La versión esperada no puede ser nula")
    private Long expectedVersion;

    public MovimientoDTO() {
    }

    public MovimientoDTO(Integer row, Integer col, Long expectedVersion) {
        this.row = row;
        this.col = col;
        this.expectedVersion = expectedVersion;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Long getExpectedVersion() {
        return expectedVersion;
    }

    public void setExpectedVersion(Long expectedVersion) {
        this.expectedVersion = expectedVersion;
    }
}
