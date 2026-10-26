package com.quizgame.service;

import com.quizgame.dto.*;
import com.quizgame.exception.BadRequestException;
import com.quizgame.exception.DuplicateSubmissionException;
import com.quizgame.exception.ResourceNotFoundException;
import com.quizgame.model.Question;
import com.quizgame.model.QuizAttempt;
import com.quizgame.repository.QuestionCatalog;
import com.quizgame.repository.QuizHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuestionCatalog questionCatalog = new QuestionCatalog();
    private final QuizHistoryRepository historyRepository;
    private final Map<String, QuizAttempt> attempts = new java.util.concurrent.ConcurrentHashMap<>();
    private final List<LeaderboardEntry> leaderboard = new ArrayList<>();

    public QuizService(QuizHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<QuizCategoryResponse> getCategories() {
        return questionCatalog.getCategoryResponses();
    }

    public List<QuestionResponse> getQuestions(String category, String difficulty) {
        String normalizedCategory = normalizeCategory(category);
        String normalizedDifficulty = normalizeDifficulty(difficulty);

        if (!questionCatalog.getCategories().contains(normalizedCategory)) {
            throw new BadRequestException("Category '" + category + "' is not supported.");
        }

        return questionCatalog.getQuestions(normalizedCategory, normalizedDifficulty)
                .stream()
                .map(this::toQuestionResponse)
                .toList();
    }

    public synchronized StartQuizResponse startQuiz(String playerName, String category, String difficulty) {
        String normalizedName = sanitizeName(playerName);
        String normalizedCategory = normalizeCategory(category);
        String normalizedDifficulty = normalizeDifficulty(difficulty);

        if (normalizedName.isBlank()) {
            throw new BadRequestException("Player name cannot be empty.");
        }

        if (!questionCatalog.getCategories().contains(normalizedCategory)) {
            throw new BadRequestException("Category '" + category + "' is not supported.");
        }

        if (!List.of("EASY", "MEDIUM", "HARD").contains(normalizedDifficulty)) {
            throw new BadRequestException("Difficulty must be EASY, MEDIUM, or HARD.");
        }

        List<Question> questions = new ArrayList<>(questionCatalog.getQuestions(normalizedCategory, normalizedDifficulty));
        if (questions.isEmpty()) {
            throw new BadRequestException("No questions are available for this category and difficulty.");
        }

        if (questions.size() < questionCatalog.getQuestionsPerQuiz()) {
            throw new IllegalStateException("The question catalog does not contain enough questions for this quiz.");
        }

        Collections.shuffle(questions);
        List<Integer> selectedQuestionIds = questions.stream()
                .limit(questionCatalog.getQuestionsPerQuiz())
                .map(Question::getId)
                .toList();

        String attemptId = UUID.randomUUID().toString();
        QuizAttempt attempt = new QuizAttempt(attemptId, normalizedName, normalizedCategory, normalizedDifficulty, selectedQuestionIds);
        attempts.put(attemptId, attempt);

        return new StartQuizResponse(attemptId, normalizedName, normalizedCategory, normalizedDifficulty, selectedQuestionIds.size());
    }

    public synchronized List<QuestionResponse> getQuestionsForAttempt(String attemptId) {
        QuizAttempt attempt = getAttemptOrThrow(attemptId);

        if (attempt.isSubmitted()) {
            throw new DuplicateSubmissionException("This quiz has already been submitted and cannot be changed.");
        }

        return getAttemptQuestions(attempt)
                .stream()
                .map(this::toQuestionResponse)
                .toList();
    }

    public synchronized SubmitQuizResultResponse submitQuiz(String attemptId, SubmitQuizRequest request) {
        QuizAttempt attempt = getAttemptOrThrow(attemptId);

        if (attempt.isSubmitted()) {
            throw new DuplicateSubmissionException("This quiz has already been submitted. Duplicate submissions are not allowed.");
        }

        if (request == null || request.getAnswers() == null) {
            throw new BadRequestException("Answers are required for submission.");
        }

        List<Question> questions = getAttemptQuestions(attempt);
        Set<Integer> validQuestionIds = questions.stream().map(Question::getId).collect(Collectors.toSet());

        for (Map.Entry<Integer, String> entry : request.getAnswers().entrySet()) {
            if (!validQuestionIds.contains(entry.getKey())) {
                throw new BadRequestException("Invalid question id provided: " + entry.getKey());
            }

            String answer = entry.getValue();
            if (answer == null || answer.isBlank()) {
                continue;
            }

            Question question = questionCatalog.findQuestionById(entry.getKey());
            if (question == null || !question.getOptions().contains(answer)) {
                throw new BadRequestException("Answer for question " + entry.getKey() + " is not valid.");
            }
        }

        int correctAnswers = 0;
        int incorrectAnswers = 0;
        int unansweredQuestions = 0;
        List<QuestionReviewItem> review = new ArrayList<>();

        for (Question question : questions) {
            String selectedAnswer = request.getAnswers().get(question.getId());
            String normalizedAnswer = selectedAnswer == null ? null : selectedAnswer.trim();
            boolean isCorrect = normalizedAnswer != null && question.getCorrectAnswer().equalsIgnoreCase(normalizedAnswer);

            if (normalizedAnswer == null || normalizedAnswer.isBlank()) {
                unansweredQuestions++;
            } else if (isCorrect) {
                correctAnswers++;
            } else {
                incorrectAnswers++;
            }

            if (normalizedAnswer == null || normalizedAnswer.isBlank()) {
                attempt.getSelectedAnswers().put(question.getId(), "");
            } else {
                attempt.getSelectedAnswers().put(question.getId(), normalizedAnswer);
            }

            review.add(new QuestionReviewItem(
                    question.getId(),
                    question.getQuestionText(),
                    normalizedAnswer == null || normalizedAnswer.isBlank() ? "No answer selected" : normalizedAnswer,
                    question.getCorrectAnswer(),
                    isCorrect,
                    question.getExplanation()
            ));
        }

        int totalQuestions = questions.size();
        int score = correctAnswers;
        double percentage = totalQuestions == 0 ? 0.0 : (score * 100.0) / totalQuestions;
        String resultMessage = buildResultMessage(score, totalQuestions);
        LocalDateTime submittedAt = LocalDateTime.now();

        historyRepository.append(new com.quizgame.model.QuizHistoryEntry(attempt.getPlayerName(), score, submittedAt));
        attempt.setSubmitted(true);
        attempt.setSubmittedAt(submittedAt);

        leaderboard.removeIf(entry ->
                entry.getPlayerName().equalsIgnoreCase(attempt.getPlayerName())
                        && entry.getCategory().equalsIgnoreCase(attempt.getCategory())
                        && entry.getDifficulty().equalsIgnoreCase(attempt.getDifficulty())
                        && entry.getScore() == score
        );

        leaderboard.add(new LeaderboardEntry(attempt.getPlayerName(), attempt.getCategory(), attempt.getDifficulty(), score, percentage));
        leaderboard.sort(Comparator.comparingDouble(LeaderboardEntry::getPercentage).reversed());

        return new SubmitQuizResultResponse(
                attempt.getAttemptId(),
                attempt.getPlayerName(),
                attempt.getCategory(),
                attempt.getDifficulty(),
                totalQuestions,
                correctAnswers,
                incorrectAnswers,
                unansweredQuestions,
                score,
                percentage,
                resultMessage,
                review
        );
    }

    public synchronized SubmitQuizResultResponse getResults(String attemptId) {
        QuizAttempt attempt = getAttemptOrThrow(attemptId);

        if (!attempt.isSubmitted()) {
            throw new BadRequestException("Quiz not yet submitted. Please complete the quiz before viewing results.");
        }

        List<Question> questions = getAttemptQuestions(attempt);
        int totalQuestions = questions.size();
        int correctAnswers = 0;
        int incorrectAnswers = 0;
        int unansweredQuestions = 0;
        List<QuestionReviewItem> review = new ArrayList<>();

        for (Question question : questions) {
            String selectedAnswer = attempt.getSelectedAnswers().getOrDefault(question.getId(), "");
            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(selectedAnswer);

            if (selectedAnswer == null || selectedAnswer.isBlank()) {
                unansweredQuestions++;
            } else if (isCorrect) {
                correctAnswers++;
            } else {
                incorrectAnswers++;
            }

            review.add(new QuestionReviewItem(
                    question.getId(),
                    question.getQuestionText(),
                    selectedAnswer == null || selectedAnswer.isBlank() ? "No answer selected" : selectedAnswer,
                    question.getCorrectAnswer(),
                    isCorrect,
                    question.getExplanation()
            ));
        }

        int score = correctAnswers;
        double percentage = totalQuestions == 0 ? 0.0 : (score * 100.0) / totalQuestions;
        String resultMessage = buildResultMessage(score, totalQuestions);

        return new SubmitQuizResultResponse(
                attempt.getAttemptId(),
                attempt.getPlayerName(),
                attempt.getCategory(),
                attempt.getDifficulty(),
                totalQuestions,
                correctAnswers,
                incorrectAnswers,
                unansweredQuestions,
                score,
                percentage,
                resultMessage,
                review
        );
    }

    public synchronized List<LeaderboardEntry> getLeaderboard() {
        return leaderboard.stream()
                .sorted(Comparator.comparingDouble(LeaderboardEntry::getPercentage).reversed())
                .limit(10)
                .toList();
    }

    public List<com.quizgame.model.QuizHistoryEntry> getHistory() {
        return historyRepository.findAll();
    }

    public byte[] exportHistory() {
        return historyRepository.exportWorkbook();
    }

    private QuizAttempt getAttemptOrThrow(String attemptId) {
        if (attemptId == null || attemptId.isBlank()) {
            throw new BadRequestException("Attempt ID is required.");
        }

        QuizAttempt attempt = attempts.get(attemptId);
        if (attempt == null) {
            throw new ResourceNotFoundException("Quiz session with ID '" + attemptId + "' was not found.");
        }

        return attempt;
    }

    private List<Question> getAttemptQuestions(QuizAttempt attempt) {
        return attempt.getQuestionIds().stream()
                .map(questionCatalog::findQuestionById)
                .filter(Objects::nonNull)
                .toList();
    }

    private QuestionResponse toQuestionResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestionText(),
                question.getCategory(),
                question.getDifficulty(),
                question.getOptions()
        );
    }

    private String sanitizeName(String name) {
        return name == null ? "" : name.trim();
    }

    private String normalizeCategory(String category) {
        return category == null ? "" : category.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeDifficulty(String difficulty) {
        return difficulty == null ? "" : difficulty.trim().toUpperCase(Locale.ROOT);
    }

    private String buildResultMessage(int score, int totalQuestions) {
        if (totalQuestions == 0) {
            return "No questions available.";
        }

        double percentage = (score * 100.0) / totalQuestions;

        if (percentage >= 80) {
            return "Excellent work! You are doing great.";
        }
        if (percentage >= 60) {
            return "Nice effort! You have a solid understanding.";
        }
        if (percentage >= 40) {
            return "Good try! Keep practicing and you will improve.";
        }
        if (percentage > 0) {
            return "You scored some points. Review the explanations and try again.";
        }
        return "No correct answers this time. Review the explanations and try again.";
    }
}
