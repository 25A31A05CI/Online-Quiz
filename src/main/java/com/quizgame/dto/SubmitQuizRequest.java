package com.quizgame.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class SubmitQuizRequest {

    @NotNull(message = "Answers are required.")
    private Map<Integer, String> answers;

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, String> answers) {
        this.answers = answers;
    }
}
