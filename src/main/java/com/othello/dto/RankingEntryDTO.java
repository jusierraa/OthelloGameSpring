package com.othello.dto;

/**
 * DTO para la tabla de ranking consumida por el frontend.
 */
public class RankingEntryDTO {
    private String id;
    private String player;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Integer games;
    private Double winRate;

    public RankingEntryDTO() {
    }

    public RankingEntryDTO(String id, String player, Integer wins, Integer losses, Integer draws,
                           Integer games, Double winRate) {
        this.id = id;
        this.player = player;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.games = games;
        this.winRate = winRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getGames() {
        return games;
    }

    public void setGames(Integer games) {
        this.games = games;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }
}
