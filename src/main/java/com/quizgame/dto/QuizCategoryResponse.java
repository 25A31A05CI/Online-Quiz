package com.quizgame.dto;

public class QuizCategoryResponse {
    private String category;
    private String label;

    public QuizCategoryResponse() {
    }

    public QuizCategoryResponse(String category, String label) {
        this.category = category;
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
