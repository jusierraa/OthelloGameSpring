package com.othello.controller;

import com.othello.dto.RankingEntryDTO;
import com.othello.model.Usuario;
import com.othello.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

/**
 * Controlador REST para el ranking de usuarios.
 */
@RestController
@RequestMapping("/ranking")
@CrossOrigin(origins = "*")
public class RankingController {

    private final UsuarioRepository usuarioRepository;

    public RankingController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<RankingEntryDTO> obtenerRanking() {
        return usuarioRepository.findAll().stream()
            .filter(Usuario::isActivo)
            .map(this::mapearEntrada)
            .sorted(
                Comparator.comparing(RankingEntryDTO::getWins).reversed()
                    .thenComparing(RankingEntryDTO::getWinRate, Comparator.reverseOrder())
                    .thenComparing(RankingEntryDTO::getPlayer, String.CASE_INSENSITIVE_ORDER)
            )
            .toList();
    }

    private RankingEntryDTO mapearEntrada(Usuario usuario) {
        int wins = valorSeguro(usuario.getPartidasGanadas());
        int losses = valorSeguro(usuario.getPartidasPerdidas());
        int draws = valorSeguro(usuario.getPartidasEmpatadas());
        int games = valorSeguro(usuario.getPartidasJugadas());
        double winRate = games == 0 ? 0D : Math.round(((double) wins / games) * 1000D) / 10D;

        return new RankingEntryDTO(
            usuario.getId(),
            usuario.getUsername(),
            wins,
            losses,
            draws,
            games,
            winRate
        );
    }

    private int valorSeguro(Integer valor) {
        return valor == null ? 0 : valor;
    }
}
