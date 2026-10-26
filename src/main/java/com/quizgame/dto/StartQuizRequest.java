package com.quizgame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StartQuizRequest {

    @NotBlank(message = "Player name is required.")
    @Size(min = 2, max = 50, message = "Player name must be between 2 and 50 characters long.")
    private String playerName;

    @NotBlank(message = "Category is required.")
    private String category;

    @NotBlank(message = "Difficulty is required.")
    private String difficulty;

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
}
