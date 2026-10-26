const resultContainer = document.getElementById('reviewContainer');
const resultMessage = document.getElementById('resultMessage');
const resultSubtitle = document.getElementById('resultSubtitle');
const playerNameResult = document.getElementById('playerNameResult');
const correctCount = document.getElementById('correctCount');
const incorrectCount = document.getElementById('incorrectCount');
const unansweredCount = document.getElementById('unansweredCount');
const scoreValue = document.getElementById('scoreValue');
const percentageValue = document.getElementById('percentageValue');
const playAgainButton = document.getElementById('playAgainButton');

const attemptData = JSON.parse(sessionStorage.getItem('quizAttempt') || '{}');
const attemptId = attemptData.attemptId || '';

async function loadResults() {
    try {
        let response;
        const storedResults = sessionStorage.getItem('quizResults');

        if (storedResults) {
            const parsed = JSON.parse(storedResults);
            renderResults(parsed);
            return;
        }

        if (!attemptId) {
            window.location.href = 'index.html';
            return;
        }

        response = await fetch(`/api/quiz/${attemptId}/results`);
        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Unable to load your results.');
        }

        renderResults(data);
    } catch (error) {
        resultMessage.textContent = error.message || 'Unable to load your results.';
        resultMessage.classList.add('error');
    }
}

function renderResults(data) {
    if (!data) {
        return;
    }

    playerNameResult.textContent = data.playerName || 'Player';
    correctCount.textContent = data.correctAnswers ?? 0;
    incorrectCount.textContent = data.incorrectAnswers ?? 0;
    unansweredCount.textContent = data.unansweredQuestions ?? 0;
    scoreValue.textContent = data.score ?? 0;
    percentageValue.textContent = `${Number(data.percentage ?? 0).toFixed(1)}%`;
    resultMessage.textContent = data.resultMessage || 'Great job!';
    resultSubtitle.textContent = `${data.category || 'Assessment'} / ${data.difficulty || 'Mixed'} · ${data.totalQuestions ?? 0} questions · Results calculated by the assessment server.`;

    resultContainer.innerHTML = '';
    data.review?.forEach((item) => {
        const entry = document.createElement('div');
        entry.className = 'review-item';

        const heading = document.createElement('h4');
        heading.textContent = `Question ${item.questionId}`;

        const questionText = document.createElement('p');
        questionText.textContent = item.questionText;

        const selected = document.createElement('p');
        const selectedLabel = document.createElement('strong');
        selectedLabel.textContent = 'Your answer: ';
        selected.append(selectedLabel, document.createTextNode(item.selectedAnswer));

        const correct = document.createElement('p');
        if (item.correct) {
            correct.className = 'correct';
            correct.textContent = 'Correct answer.';
        } else {
            correct.className = 'wrong';
            correct.textContent = `Incorrect. Correct answer: ${item.correctAnswer}`;
        }

        const explanation = document.createElement('p');
        explanation.textContent = `Explanation: ${item.explanation}`;

        entry.append(heading, questionText, selected, correct, explanation);
        resultContainer.appendChild(entry);
    });
}

playAgainButton.addEventListener('click', () => {
    sessionStorage.removeItem('quizAttempt');
    sessionStorage.removeItem('quizResults');
    window.location.href = 'index.html';
});

loadResults();
