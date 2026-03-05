package com.othello.repository;

import com.othello.model.EstadoJuego;
import com.othello.model.Juego;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio MongoDB para almacenar juegos.
 * Cumple con SRP: Solo maneja el almacenamiento de juegos.
 */
@Repository
public interface JuegoRepository extends MongoRepository<Juego, String> {
    
    /**
     * Busca juegos por estado.
     */
    List<Juego> findByEstado(EstadoJuego estado);
    
    /**
     * Busca juegos creados después de una fecha.
     */
    List<Juego> findByFechaCreacionAfter(LocalDateTime fecha);

    /**
     * Busca juegos pertenecientes a un usuario.
     */
    List<Juego> findByOwnerUsernameOrderByFechaUltimoMovimientoDesc(String ownerUsername);
}
