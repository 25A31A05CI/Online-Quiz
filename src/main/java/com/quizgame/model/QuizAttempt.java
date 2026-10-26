package com.quizgame.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizAttempt {
    private String attemptId;
    private String playerName;
    private String category;
    private String difficulty;
    private List<Integer> questionIds = List.of();
    private Map<Integer, String> selectedAnswers = new HashMap<>();
    private boolean submitted;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;

    public QuizAttempt() {
    }

    public QuizAttempt(String attemptId, String playerName, String category, String difficulty) {
        this.attemptId = attemptId;
        this.playerName = playerName;
        this.category = category;
        this.difficulty = difficulty;
        this.startedAt = LocalDateTime.now();
    }

    public QuizAttempt(String attemptId, String playerName, String category, String difficulty, List<Integer> questionIds) {
        this(attemptId, playerName, category, difficulty);
        this.questionIds = List.copyOf(questionIds);
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

    public List<Integer> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Integer> questionIds) {
        this.questionIds = List.copyOf(questionIds);
    }

    public Map<Integer, String> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(Map<Integer, String> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
