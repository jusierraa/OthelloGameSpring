package com.othello.dto;

/**
 * DTO para respuestas de la API.
 */
public class RespuestaDTO {
    private boolean exito;
    private String mensaje;
    private Object data;

    public RespuestaDTO() {
    }

    public RespuestaDTO(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public RespuestaDTO(boolean exito, String mensaje, Object data) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.data = data;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
