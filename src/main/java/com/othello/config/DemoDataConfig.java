package com.othello.config;

import com.othello.model.Usuario;
import com.othello.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Crea usuarios demo para que el frontend pueda iniciar sesión sin preparación manual.
 */
@Configuration
public class DemoDataConfig {

    @Bean
    public CommandLineRunner seedDemoUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<DemoUser> demoUsers = List.of(
                new DemoUser("ada", "ada@reversi.local", "reversi123", 26, 8, 2),
                new DemoUser("grace", "grace@reversi.local", "boardmaster", 26, 10, 4),
                new DemoUser("mina", "mina@reversi.local", "reversi123", 21, 9, 3),
                new DemoUser("leo", "leo@reversi.local", "reversi123", 18, 6, 2),
                new DemoUser("noor", "noor@reversi.local", "reversi123", 15, 11, 4)
            );

            for (DemoUser demoUser : demoUsers) {
                if (usuarioRepository.existsByUsername(demoUser.username())) {
                    continue;
                }

                Usuario usuario = new Usuario();
                usuario.setUsername(demoUser.username());
                usuario.setEmail(demoUser.email());
                usuario.setPassword(passwordEncoder.encode(demoUser.password()));
                usuario.setFechaRegistro(LocalDateTime.now());
                usuario.setUltimoAcceso(LocalDateTime.now());
                usuario.setPartidasGanadas(demoUser.wins());
                usuario.setPartidasPerdidas(demoUser.losses());
                usuario.setPartidasEmpatadas(demoUser.draws());
                usuario.setPartidasJugadas(demoUser.wins() + demoUser.losses() + demoUser.draws());
                usuarioRepository.save(usuario);
            }
        };
    }

    private record DemoUser(String username, String email, String password, int wins, int losses, int draws) {
    }
}
