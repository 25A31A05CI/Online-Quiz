package com.quizgame.model;

import java.time.LocalDateTime;

public record QuizHistoryEntry(String candidateName, int score, LocalDateTime time) {
}