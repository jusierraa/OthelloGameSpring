package com.othello.repository;

import com.othello.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio MongoDB para usuarios.
 */
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    
    /**
     * Busca un usuario por su username.
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Busca un usuario por su email.
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con ese username.
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con ese email.
     */
    boolean existsByEmail(String email);
}
