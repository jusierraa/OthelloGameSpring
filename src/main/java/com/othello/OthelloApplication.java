package com.othello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot para el juego de Othello.
 * 
 * @author Grupo 6
 */
@SpringBootApplication
public class OthelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(OthelloApplication.class, args);
        System.out.println("==================================================");
        System.out.println("    🎮 Othello Backend API Iniciado");
        System.out.println("    📍 Disponible en: http://localhost:8080/api");
        System.out.println("==================================================");
    }
}
