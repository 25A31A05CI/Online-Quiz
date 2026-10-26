package com.quizgame.dto;

import java.util.List;

public class SubmitQuizResultResponse {
    private String attemptId;
    private String playerName;
    private String category;
    private String difficulty;
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private int unansweredQuestions;
    private int score;
    private double percentage;
    private String resultMessage;
    private List<QuestionReviewItem> review;

    public SubmitQuizResultResponse() {
    }

    public SubmitQuizResultResponse(String attemptId, String playerName, String category, String difficulty,
                                   int totalQuestions, int correctAnswers, int incorrectAnswers,
                                   int unansweredQuestions, int score, double percentage,
                                   String resultMessage, List<QuestionReviewItem> review) {
        this.attemptId = attemptId;
        this.playerName = playerName;
        this.category = category;
        this.difficulty = difficulty;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.unansweredQuestions = unansweredQuestions;
        this.score = score;
        this.percentage = percentage;
        this.resultMessage = resultMessage;
        this.review = review;
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

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getUnansweredQuestions() {
        return unansweredQuestions;
    }

    public void setUnansweredQuestions(int unansweredQuestions) {
        this.unansweredQuestions = unansweredQuestions;
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

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<QuestionReviewItem> getReview() {
        return review;
    }

    public void setReview(List<QuestionReviewItem> review) {
        this.review = review;
    }
}
