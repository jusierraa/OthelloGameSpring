package com.othello.repository;

import com.othello.model.Juego;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repositorio en memoria para almacenar juegos.
 * Cumple con SRP: Solo maneja el almacenamiento de juegos.
 */
@Repository
public class JuegoRepository {
    
    private final ConcurrentHashMap<String, Juego> juegos = new ConcurrentHashMap<>();

    public Juego guardar(Juego juego) {
        juegos.put(juego.getId(), juego);
        return juego;
    }

    public Optional<Juego> buscarPorId(String id) {
        return Optional.ofNullable(juegos.get(id));
    }

    public List<Juego> buscarTodos() {
        return new ArrayList<>(juegos.values());
    }

    public void eliminar(String id) {
        juegos.remove(id);
    }

    public boolean existe(String id) {
        return juegos.containsKey(id);
    }
}
