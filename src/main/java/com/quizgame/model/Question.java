package com.quizgame.model;

import java.util.List;

public class Question {
    private final int id;
    private final String category;
    private final String difficulty;
    private final String questionText;
    private final List<String> options;
    private final String correctAnswer;
    private final String explanation;

    public Question(int id, String category, String difficulty, String questionText,
                    List<String> options, String correctAnswer, String explanation) {
        this.id = id;
        this.category = category;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.options = List.copyOf(options);
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }
}
