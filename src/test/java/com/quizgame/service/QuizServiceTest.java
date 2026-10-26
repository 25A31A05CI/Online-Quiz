package com.quizgame.service;

import com.quizgame.dto.StartQuizResponse;
import com.quizgame.dto.QuestionResponse;
import com.quizgame.dto.SubmitQuizRequest;
import com.quizgame.dto.SubmitQuizResultResponse;
import com.quizgame.exception.BadRequestException;
import com.quizgame.exception.DuplicateSubmissionException;
import com.quizgame.model.Question;
import com.quizgame.repository.QuestionCatalog;
import com.quizgame.repository.QuizHistoryRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QuizServiceTest {

    @TempDir
    Path tempDir;

    private QuizService quizService;
    private QuizHistoryRepository historyRepository;

    @BeforeEach
    void setUp() {
        historyRepository = new QuizHistoryRepository(tempDir.resolve("history.xlsx").toString());
        historyRepository.initialize();
        quizService = new QuizService(historyRepository);
    }

    @Test
    void everyCategoryAndDifficultyShouldStartWithFifteenStableQuestions() {
        List<String> categories = List.of("JAVA", "HTML", "CSS", "JAVASCRIPT", "GENERAL_CS");
        List<String> difficulties = List.of("EASY", "MEDIUM", "HARD");

        for (String category : categories) {
            for (String difficulty : difficulties) {
                StartQuizResponse started = quizService.startQuiz("Alice", category, difficulty);
                List<QuestionResponse> firstFetch = quizService.getQuestionsForAttempt(started.getAttemptId());
                List<QuestionResponse> secondFetch = quizService.getQuestionsForAttempt(started.getAttemptId());

                assertEquals(15, started.getTotalQuestions());
                assertEquals(15, firstFetch.size());
                assertEquals(
                        firstFetch.stream().map(QuestionResponse::getId).toList(),
                        secondFetch.stream().map(QuestionResponse::getId).toList());
                assertTrue(firstFetch.stream().allMatch(question ->
                        question.getCategory().equals(category) && question.getDifficulty().equals(difficulty)));
            }
        }

        assertTrue(List.of(QuestionResponse.class.getDeclaredFields()).stream()
                .noneMatch(field -> field.getName().toLowerCase().contains("correct")));
    }

    @Test
    void startQuizShouldRejectEmptyPlayerName() {
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> quizService.startQuiz("   ", "JAVA", "EASY"));

        assertTrue(ex.getMessage().contains("Player name"));
    }

    @Test
    void submitQuizShouldCalculateCorrectScoreAndUnansweredCountOnServer() {
        StartQuizResponse started = quizService.startQuiz("Bob", "JAVA", "EASY");
        List<QuestionResponse> visibleQuestions = quizService.getQuestionsForAttempt(started.getAttemptId());
        QuestionCatalog catalog = new QuestionCatalog();

        SubmitQuizRequest request = new SubmitQuizRequest();
        Map<Integer, String> answers = new HashMap<>();
        Question correctQuestion = catalog.findQuestionById(visibleQuestions.get(0).getId());
        Question incorrectQuestion = catalog.findQuestionById(visibleQuestions.get(1).getId());
        answers.put(correctQuestion.getId(), correctQuestion.getCorrectAnswer());
        answers.put(incorrectQuestion.getId(), incorrectQuestion.getOptions().stream()
                .filter(option -> !option.equals(incorrectQuestion.getCorrectAnswer()))
                .findFirst()
                .orElseThrow());
        request.setAnswers(answers);

        SubmitQuizResultResponse result = quizService.submitQuiz(started.getAttemptId(), request);

        assertEquals(15, result.getTotalQuestions());
        assertEquals(1, result.getCorrectAnswers());
        assertEquals(1, result.getIncorrectAnswers());
        assertEquals(13, result.getUnansweredQuestions());
        assertEquals(result.getTotalQuestions(),
                result.getCorrectAnswers() + result.getIncorrectAnswers() + result.getUnansweredQuestions());
        assertEquals(1, result.getScore());
    }

    @Test
    void submitQuizShouldAllowAllQuestionsToRemainUnanswered() {
        StartQuizResponse started = quizService.startQuiz("Taylor", "CSS", "HARD");
        SubmitQuizRequest request = new SubmitQuizRequest();
        request.setAnswers(Map.of());

        SubmitQuizResultResponse result = quizService.submitQuiz(started.getAttemptId(), request);

        assertEquals(0, result.getCorrectAnswers());
        assertEquals(0, result.getIncorrectAnswers());
        assertEquals(15, result.getUnansweredQuestions());
        assertEquals(0.0, result.getPercentage());
    }

    @Test
    void submitQuizShouldRejectAnAnswerNotAmongThatQuestionsOptions() {
        StartQuizResponse started = quizService.startQuiz("Morgan", "HTML", "MEDIUM");
        int questionId = quizService.getQuestionsForAttempt(started.getAttemptId()).get(0).getId();
        SubmitQuizRequest request = new SubmitQuizRequest();
        request.setAnswers(Map.of(questionId, "not an option"));

        assertThrows(BadRequestException.class, () -> quizService.submitQuiz(started.getAttemptId(), request));
    }

    @Test
    void submitQuizShouldRejectDuplicateSubmission() {
        StartQuizResponse started = quizService.startQuiz("Charlie", "HTML", "EASY");
        QuestionCatalog catalog = new QuestionCatalog();
        int questionId = quizService.getQuestionsForAttempt(started.getAttemptId()).get(0).getId();
        String validOption = catalog.findQuestionById(questionId).getOptions().get(0);

        SubmitQuizRequest firstRequest = new SubmitQuizRequest();
        firstRequest.setAnswers(Map.of(questionId, validOption));

        quizService.submitQuiz(started.getAttemptId(), firstRequest);

        SubmitQuizRequest secondRequest = new SubmitQuizRequest();
        secondRequest.setAnswers(Map.of(questionId, validOption));

        DuplicateSubmissionException ex = assertThrows(DuplicateSubmissionException.class,
                () -> quizService.submitQuiz(started.getAttemptId(), secondRequest));

        assertTrue(ex.getMessage().contains("already been submitted"));
    }

    @Test
    void completedAttemptShouldBeRecordedAndExportedToExcel() throws Exception {
        StartQuizResponse started = quizService.startQuiz("Asha", "JAVA", "EASY");
        int questionId = quizService.getQuestionsForAttempt(started.getAttemptId()).get(0).getId();
        Question question = new QuestionCatalog().findQuestionById(questionId);
        SubmitQuizRequest request = new SubmitQuizRequest();
        request.setAnswers(Map.of(questionId, question.getCorrectAnswer()));

        quizService.submitQuiz(started.getAttemptId(), request);

        assertEquals(1, historyRepository.findAll().size());
        assertEquals("Asha", historyRepository.findAll().get(0).candidateName());
        assertEquals(1, historyRepository.findAll().get(0).score());

        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(historyRepository.exportWorkbook()))) {
            assertEquals("Candidate Name", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
            assertEquals("Score", workbook.getSheetAt(0).getRow(0).getCell(1).getStringCellValue());
            assertEquals("Time", workbook.getSheetAt(0).getRow(0).getCell(2).getStringCellValue());
            assertEquals("Asha", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
            assertEquals(1.0, workbook.getSheetAt(0).getRow(1).getCell(1).getNumericCellValue());
        }

        QuizHistoryRepository reloaded = new QuizHistoryRepository(tempDir.resolve("history.xlsx").toString());
        reloaded.initialize();
        assertEquals("Asha", reloaded.findAll().get(0).candidateName());
        assertEquals(1, reloaded.findAll().get(0).score());
    }
}
