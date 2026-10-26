package com.quizgame.controller;

import com.quizgame.dto.*;
import com.quizgame.model.QuizHistoryEntry;
import com.quizgame.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<QuizCategoryResponse>> getCategories() {
        return ResponseEntity.ok(quizService.getCategories());
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty
    ) {
        return ResponseEntity.ok(quizService.getQuestions(category, difficulty));
    }

    @PostMapping("/start")
    public ResponseEntity<StartQuizResponse> startQuiz(@Valid @RequestBody StartQuizRequest request) {
        StartQuizResponse response = quizService.startQuiz(
                request.getPlayerName(),
                request.getCategory(),
                request.getDifficulty()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{attemptId}/questions")
    public ResponseEntity<List<QuestionResponse>> getAttemptQuestions(@PathVariable String attemptId) {
        return ResponseEntity.ok(quizService.getQuestionsForAttempt(attemptId));
    }

    @PostMapping("/{attemptId}/submit")
    public ResponseEntity<SubmitQuizResultResponse> submitQuiz(
            @PathVariable String attemptId,
            @Valid @RequestBody SubmitQuizRequest request
    ) {
        SubmitQuizResultResponse response = quizService.submitQuiz(attemptId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{attemptId}/results")
    public ResponseEntity<SubmitQuizResultResponse> getResults(@PathVariable String attemptId) {
        return ResponseEntity.ok(quizService.getResults(attemptId));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard() {
        return ResponseEntity.ok(quizService.getLeaderboard());
    }

    @GetMapping("/history")
    public ResponseEntity<List<QuizHistoryEntry>> getHistory() {
        return ResponseEntity.ok(quizService.getHistory());
    }

    @GetMapping("/history/export")
    public ResponseEntity<byte[]> exportHistory() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=quiz-history.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(quizService.exportHistory());
    }
}
