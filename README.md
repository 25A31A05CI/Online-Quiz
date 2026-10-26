# Online Quiz Game

A single-server assessment application built with Java 21+, Spring Boot, Maven, HTML, CSS, and vanilla JavaScript.

## Features

- Five categories: Java, HTML, CSS, JavaScript, and General Computer Science
- Easy, Medium, and Hard filters
- 15 questions selected randomly for each category/difficulty attempt
- Four answer choices per question; correct answers remain on the Java server until submission
- Previous/next navigation, skip, saved answers, progress, and server-calculated results
- Review of selected and correct answers with explanations
- 30-second timer for each question; timeout advances automatically
- Candidate attempt history with an Excel workbook download
- A temporary in-memory leaderboard
- Fullscreen request and focus interruption monitoring
- Responsive home, exam, and score-report screens

There are 15 authored questions for each of the 15 category/difficulty combinations (225 question records total). Each attempt receives a random selection of 15 questions, and the server stores that selection so retrieval, scoring, and review use the same questions.

## Technologies

- Java 21 or newer
- Spring Boot 3.3.4
- Maven
- HTML5, CSS3, and vanilla JavaScript
- Java in-memory storage; no external database or frontend framework

## Project Structure

```text
online-quiz-game/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/wrapper/maven-wrapper.properties
├── README.md
└── src/
    ├── main/
    │   ├── java/com/quizgame/
    │   │   ├── QuizGameApplication.java
    │   │   ├── config/
    │   │   ├── controller/
    │   │   ├── dto/
    │   │   ├── exception/
    │   │   ├── model/
    │   │   ├── repository/QuestionCatalog.java
    │   │   └── service/QuizService.java
    │   └── resources/
    │       ├── application.properties
    │       └── static/
    │           ├── index.html
    │           ├── quiz.html
    │           ├── result.html
    │           ├── css/style.css
    │           └── js/
    │               ├── app.js
    │               ├── quiz.js
    │               └── result.js
    └── test/java/com/quizgame/service/QuizServiceTest.java
```

## Windows Setup and Run

1. Install JDK 21 LTS (Temurin or Oracle JDK) and verify in a VS Code terminal:
   ```powershell
   java -version
   javac -version
   ```
2. Install Visual Studio Code and its Java Extension Pack. Maven does not require Node.js.
3. Open `C:\Users\SS\Desktop\course\online-quiz-game` in VS Code.
4. In the project terminal, build and test:
   ```powershell
   mvn clean verify
   ```
   If Maven is not on PATH but is installed with the Oracle Java extension, run:
   ```powershell
   & "$env:USERPROFILE\.vscode\extensions\oracle.oracle-java-27.0.0\nbcode\java\maven\bin\mvn.cmd" clean verify
   ```
5. Start the app from the project root:
   ```powershell
   mvn spring-boot:run
   ```
   Or use the Maven command above followed by `spring-boot:run`.
6. Open **http://localhost:8081/** in Chrome or Edge.

The project contains `mvnw` launch scripts, which use Maven from PATH when available. A Maven Wrapper JAR is not committed in this project, so these scripts need Maven installed unless you generate the wrapper locally.

## Fullscreen and Focus Behavior

The browser only allows fullscreen to begin after a user action. The assessment asks for fullscreen when **Begin assessment** is clicked. If the browser refuses fullscreen, the user can retry or continue without it.

Websites cannot prevent switching tabs, changing applications, using browser shortcuts, or exiting fullscreen. This app can detect page visibility changes and exiting the requested fullscreen mode, but this is monitoring rather than a secure lockdown. The first detected interruption displays a warning; the second submits the current answers. A single tab change that also exits fullscreen is debounced to avoid counting as two interruptions.

## Data Storage

Question records, active attempts, selected question IDs, answers, and leaderboard entries are stored in the running Java process and reset when the server restarts. Completed attempt history is stored separately in `data/quiz-history.xlsx` and is loaded again when the server starts. The workbook contains Candidate Name, Score (number correct), and Time (submission date/time). Keep the `data` folder with the project to retain the workbook.

The history page is available from the home/results navigation at `/history.html`. The Excel download endpoint is `/api/quiz/history/export`.

## REST API

- `GET /api/quiz/categories` — available categories
- `GET /api/quiz/questions?category=JAVA&difficulty=EASY` — filtered questions (without correct answers)
- `POST /api/quiz/start` — create a quiz attempt
- `GET /api/quiz/{attemptId}/questions` — get the attempt's fixed 15 questions
- `POST /api/quiz/{attemptId}/submit` — submit answers; score is calculated by Java
- `GET /api/quiz/{attemptId}/results` — retrieve submitted results and review
- `GET /api/quiz/leaderboard` — temporary top scores
- `GET /api/quiz/history` — completed attempts, newest first
- `GET /api/quiz/history/export` — download the XLSX history workbook

Example start request:

```json
{
  "playerName": "Asha",
  "category": "JAVA",
  "difficulty": "EASY"
}
```

Example submit request (question IDs are specific to the attempt):

```json
{
  "answers": {
    "4": "class",
    "8": "public static void main(String[] args)"
  }
}
```

Unanswered questions are allowed and included in the result summary. Repeated submissions return HTTP 409 Conflict.

## Testing

Run the backend suite and packaging check from the project root:

```powershell
mvn clean verify
```

The tests cover all category/difficulty filters, 15-question attempts, stable question selection, answer validation, server-side scoring, unanswered results, duplicate-submission rejection, and XLSX history persistence/export.

For a browser smoke test, start the application and check that categories load, each quiz shows `Question 1 of 15`, each question counts down from 30 seconds and advances on timeout, answers survive Previous/Next navigation, and results display a 15-question review. Submit an attempt, open `/history.html`, and download the workbook. Verify fullscreen in Chrome/Edge after clicking Begin assessment; fullscreen and tab monitoring behavior is browser-dependent.