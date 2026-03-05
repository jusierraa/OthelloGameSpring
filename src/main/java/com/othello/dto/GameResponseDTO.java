package com.othello.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO para la respuesta del juego con formato {"game": {...}}.
 */
public class GameResponseDTO {
    private GameDTO game;

    public GameResponseDTO() {
    }

    public GameResponseDTO(GameDTO game) {
        this.game = game;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    /**
     * Clase interna que contiene los datos del juego.
     */
    public static class GameDTO {
        private String id;
        private String[][] board;
        private String currentPlayer;
        private String status;
        private Integer passCount;
        private String winner;
        private Map<String, Integer> score;
        private List<PosicionDTO> validMoves;
        private Long version;
        private String message;
        private Boolean isGameOver;

        public GameDTO() {
        }

        public GameDTO(String id, String[][] board, String currentPlayer, String status,
                       Integer passCount, String winner, Map<String, Integer> score,
                       List<PosicionDTO> validMoves, Long version, String message, Boolean isGameOver) {
            this.id = id;
            this.board = board;
            this.currentPlayer = currentPlayer;
            this.status = status;
            this.passCount = passCount;
            this.winner = winner;
            this.score = score;
            this.validMoves = validMoves;
            this.version = version;
            this.message = message;
            this.isGameOver = isGameOver;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String[][] getBoard() {
            return board;
        }

        public void setBoard(String[][] board) {
            this.board = board;
        }

        public String getCurrentPlayer() {
            return currentPlayer;
        }

        public void setCurrentPlayer(String currentPlayer) {
            this.currentPlayer = currentPlayer;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getPassCount() {
            return passCount;
        }

        public void setPassCount(Integer passCount) {
            this.passCount = passCount;
        }

        public String getWinner() {
            return winner;
        }

        public void setWinner(String winner) {
            this.winner = winner;
        }

        public Map<String, Integer> getScore() {
            return score;
        }

        public void setScore(Map<String, Integer> score) {
            this.score = score;
        }

        public List<PosicionDTO> getValidMoves() {
            return validMoves;
        }

        public void setValidMoves(List<PosicionDTO> validMoves) {
            this.validMoves = validMoves;
        }

        public Long getVersion() {
            return version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getIsGameOver() {
            return isGameOver;
        }

        public void setIsGameOver(Boolean gameOver) {
            isGameOver = gameOver;
        }
    }
}
