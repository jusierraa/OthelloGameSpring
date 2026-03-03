package com.othello.service.impl;

import com.othello.config.JwtTokenProvider;
import com.othello.dto.AuthResponseDTO;
import com.othello.dto.LoginRequestDTO;
import com.othello.dto.RegisterRequestDTO;
import com.othello.model.Usuario;
import com.othello.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio de autenticación y registro de usuarios.
 */
@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    public AuthService(UsuarioRepository usuarioRepository,
                      PasswordEncoder passwordEncoder,
                      AuthenticationManager authenticationManager,
                      JwtTokenProvider tokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }
    
    /**
     * Registra un nuevo usuario.
     */
    public AuthResponseDTO registrarUsuario(RegisterRequestDTO request) {
        // Validar que el username no exista
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }
        
        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.agregarRole("ROLE_USER");
        
        // Guardar en base de datos
        usuarioRepository.save(usuario);
        
        logger.info("Usuario registrado: {}", usuario.getUsername());
        
        // Autenticar automáticamente
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generar token JWT
        String jwt = tokenProvider.generateToken(authentication);
        
        return new AuthResponseDTO(jwt, usuario.getUsername(), usuario.getEmail(), usuario.getRoles());
    }
    
    /**
     * Inicia sesión con un usuario existente.
     */
    public AuthResponseDTO login(LoginRequestDTO request) {
        // Autenticar
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Actualizar último acceso
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        logger.info("Usuario autenticado: {}", usuario.getUsername());
        
        // Generar token JWT
        String jwt = tokenProvider.generateToken(authentication);
        
        return new AuthResponseDTO(jwt, usuario.getUsername(), usuario.getEmail(), usuario.getRoles());
    }
}
