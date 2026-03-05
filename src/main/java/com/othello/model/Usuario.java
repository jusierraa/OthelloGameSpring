package com.othello.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Usuario para sistema de autenticación.
 */
@Document(collection = "usuarios")
public class Usuario {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String username;
    
    @Indexed(unique = true)
    private String email;
    
    private String password;  // Hasheado con BCrypt
    
    private Set<String> roles = new HashSet<>();
    
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
    
    // Estadísticas del jugador
    private Integer partidasJugadas = 0;
    private Integer partidasGanadas = 0;
    private Integer partidasPerdidas = 0;
    private Integer partidasEmpatadas = 0;
    
    private boolean activo = true;

    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
        this.roles.add("ROLE_USER");
    }

    public Usuario(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Integer getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(Integer partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public Integer getPartidasGanadas() {
        return partidasGanadas;
    }

    public void setPartidasGanadas(Integer partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }

    public Integer getPartidasPerdidas() {
        return partidasPerdidas;
    }

    public void setPartidasPerdidas(Integer partidasPerdidas) {
        this.partidasPerdidas = partidasPerdidas;
    }

    public Integer getPartidasEmpatadas() {
        return partidasEmpatadas;
    }

    public void setPartidasEmpatadas(Integer partidasEmpatadas) {
        this.partidasEmpatadas = partidasEmpatadas;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void agregarRole(String role) {
        this.roles.add(role);
    }

    public void registrarVictoria() {
        this.partidasJugadas = valorSeguro(partidasJugadas) + 1;
        this.partidasGanadas = valorSeguro(partidasGanadas) + 1;
    }

    public void registrarDerrota() {
        this.partidasJugadas = valorSeguro(partidasJugadas) + 1;
        this.partidasPerdidas = valorSeguro(partidasPerdidas) + 1;
    }

    public void registrarEmpate() {
        this.partidasJugadas = valorSeguro(partidasJugadas) + 1;
        this.partidasEmpatadas = valorSeguro(partidasEmpatadas) + 1;
    }

    private int valorSeguro(Integer valor) {
        return valor == null ? 0 : valor;
    }
}
