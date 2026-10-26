package com.quizgame.repository;

import com.quizgame.model.QuizHistoryEntry;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class QuizHistoryRepository {

    private static final String SHEET_NAME = "Quiz History";
    private static final List<String> HEADERS = List.of("Candidate Name", "Score", "Time");

    private final Path historyFile;
    private final List<QuizHistoryEntry> entries = new ArrayList<>();

    public QuizHistoryRepository(@Value("${quiz.history.file:data/quiz-history.xlsx}") String fileName) {
        this.historyFile = Path.of(fileName).toAbsolutePath().normalize();
    }

    @PostConstruct
    public synchronized void initialize() {
        if (Files.exists(historyFile)) {
            loadWorkbook();
        } else {
            writeWorkbook(entries);
        }
    }

    public synchronized void append(QuizHistoryEntry entry) {
        List<QuizHistoryEntry> updatedEntries = new ArrayList<>(entries);
        updatedEntries.add(entry);
        writeWorkbook(updatedEntries);
        entries.clear();
        entries.addAll(updatedEntries);
    }

    public synchronized List<QuizHistoryEntry> findAll() {
        List<QuizHistoryEntry> newestFirst = new ArrayList<>(entries);
        Collections.reverse(newestFirst);
        return List.copyOf(newestFirst);
    }

    public synchronized byte[] exportWorkbook() {
        try {
            return Files.readAllBytes(historyFile);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read the quiz history workbook.", exception);
        }
    }

    private void loadWorkbook() {
        try (InputStream input = Files.newInputStream(historyFile); Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheet(SHEET_NAME);
            if (sheet == null) {
                return;
            }

            DataFormatter formatter = new DataFormatter();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || row.getCell(0) == null || row.getCell(2) == null) {
                    continue;
                }

                String candidateName = formatter.formatCellValue(row.getCell(0));
                String scoreText = formatter.formatCellValue(row.getCell(1));
                if (candidateName.isBlank() || scoreText.isBlank()) {
                    continue;
                }

                int score = (int) Double.parseDouble(scoreText);
                Date playedAt = row.getCell(2).getDateCellValue();
                LocalDateTime time = LocalDateTime.ofInstant(playedAt.toInstant(), ZoneId.systemDefault());
                entries.add(new QuizHistoryEntry(candidateName, score, time));
            }
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Unable to load quiz history workbook: " + historyFile, exception);
        }
    }

    private void writeWorkbook(List<QuizHistoryEntry> records) {
        Path parent = historyFile.getParent();
        Path temporaryFile = null;

        try {
            if (parent != null) {
                Files.createDirectories(parent);
                temporaryFile = Files.createTempFile(parent, "quiz-history-", ".xlsx");
            } else {
                temporaryFile = Files.createTempFile("quiz-history-", ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet(SHEET_NAME);
                Row header = sheet.createRow(0);
                CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
                boldFont.setBold(true);
                headerStyle.setFont(boldFont);

                for (int column = 0; column < HEADERS.size(); column++) {
                    Cell cell = header.createCell(column);
                    cell.setCellValue(HEADERS.get(column));
                    cell.setCellStyle(headerStyle);
                }

                CellStyle timeStyle = workbook.createCellStyle();
                timeStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));

                for (int index = 0; index < records.size(); index++) {
                    QuizHistoryEntry entry = records.get(index);
                    Row row = sheet.createRow(index + 1);
                    row.createCell(0).setCellValue(entry.candidateName());
                    row.createCell(1).setCellValue(entry.score());
                    Cell timeCell = row.createCell(2);
                    timeCell.setCellValue(Date.from(entry.time().atZone(ZoneId.systemDefault()).toInstant()));
                    timeCell.setCellStyle(timeStyle);
                }

                sheet.setColumnWidth(0, 34 * 256);
                sheet.setColumnWidth(1, 14 * 256);
                sheet.setColumnWidth(2, 24 * 256);

                try (OutputStream output = Files.newOutputStream(temporaryFile)) {
                    workbook.write(output);
                }
            }

            try {
                Files.move(temporaryFile, historyFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, historyFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // Preserve the original workbook error.
                }
            }
            throw new IllegalStateException("Unable to write quiz history workbook: " + historyFile, exception);
        }
    }
}