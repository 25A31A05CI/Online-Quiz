package com.quizgame.dto;

public class StartQuizResponse {
    private String attemptId;
    private String playerName;
    private String category;
    private String difficulty;
    private int totalQuestions;

    public StartQuizResponse() {
    }

    public StartQuizResponse(String attemptId, String playerName, String category, String difficulty, int totalQuestions) {
        this.attemptId = attemptId;
        this.playerName = playerName;
        this.category = category;
        this.difficulty = difficulty;
        this.totalQuestions = totalQuestions;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
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

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
