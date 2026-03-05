package com.othello.controller;

import com.othello.dto.AuthResponseDTO;
import com.othello.dto.LoginRequestDTO;
import com.othello.dto.RegisterRequestDTO;
import com.othello.model.Usuario;
import com.othello.repository.UsuarioRepository;
import com.othello.service.impl.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para endpoints de autenticación.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    
    public AuthController(AuthService authService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }
    
    /**
     * Endpoint para registrar un nuevo usuario.
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            AuthResponseDTO response = authService.registrarUsuario(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Endpoint para iniciar sesión.
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            AuthResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }
    
    /**
     * Endpoint para obtener información del usuario autenticado.
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<?> obtenerUsuarioActual(Authentication authentication) {
        if (authentication == null ||
            authentication instanceof AnonymousAuthenticationToken ||
            !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", usuario.getUsername());
        response.put("email", usuario.getEmail());
        response.put("roles", usuario.getRoles());
        response.put("partidasJugadas", usuario.getPartidasJugadas());
        response.put("partidasGanadas", usuario.getPartidasGanadas());
        response.put("partidasPerdidas", usuario.getPartidasPerdidas());
        response.put("partidasEmpatadas", usuario.getPartidasEmpatadas());
        response.put("fechaRegistro", usuario.getFechaRegistro());
        response.put("ultimoAcceso", usuario.getUltimoAcceso());
        
        return ResponseEntity.ok(response);
    }
}
