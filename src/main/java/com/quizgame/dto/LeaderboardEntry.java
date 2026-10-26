package com.quizgame.dto;

public class LeaderboardEntry {
    private String playerName;
    private String category;
    private String difficulty;
    private int score;
    private double percentage;

    public LeaderboardEntry() {
    }

    public LeaderboardEntry(String playerName, String category, String difficulty, int score, double percentage) {
        this.playerName = playerName;
        this.category = category;
        this.difficulty = difficulty;
        this.score = score;
        this.percentage = percentage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
