const attemptData = JSON.parse(sessionStorage.getItem('quizAttempt') || '{}');
const welcome = document.getElementById('examWelcome');
const examContent = document.getElementById('examContent');
const beginExamButton = document.getElementById('beginExamButton');
const continueWithoutFullscreenButton = document.getElementById('continueWithoutFullscreenButton');
const fullscreenFallback = document.getElementById('fullscreenFallback');
const candidateName = document.getElementById('candidateName');
const categoryBadge = document.getElementById('quizCategoryBadge');
const difficultyBadge = document.getElementById('quizDifficultyBadge');
const questionTotal = document.getElementById('questionTotal');
const activeCategoryBadge = document.getElementById('activeCategoryBadge');
const questionCounter = document.getElementById('questionCounter');
const progressWrap = document.querySelector('.progress-wrap');
const progressBar = document.getElementById('progressBar');
const questionText = document.getElementById('questionText');
const answerOptions = document.getElementById('answerOptions');
const prevButton = document.getElementById('prevButton');
const nextButton = document.getElementById('nextButton');
const skipButton = document.getElementById('skipButton');
const submitButton = document.getElementById('submitButton');
const quizMessage = document.getElementById('quizMessage');
const violationCount = document.getElementById('violationCount');
const resumeFullscreenButton = document.getElementById('resumeFullscreenButton');
const questionTimer = document.getElementById('questionTimer');
const timerBar = document.getElementById('timerBar');
const timerBox = document.querySelector('.timer-box');

const state = {
    attemptId: attemptData.attemptId || '',
    questions: [],
    currentIndex: 0,
    answers: {},
    timeRemainingByQuestion: Object.create(null),
    expiredQuestionIds: new Set(),
    timerInterval: null,
    timeRemaining: 30,
    started: false,
    isSubmitting: false,
    violations: 0
};

function showMessage(message, type = 'error') {
    quizMessage.textContent = message;
    quizMessage.classList.remove('hidden', 'error', 'warning', 'success');
    quizMessage.classList.add(type);
}

function clearMessage() {
    quizMessage.textContent = '';
    quizMessage.classList.add('hidden');
    quizMessage.classList.remove('error', 'warning', 'success');
}

function showFullscreenFallback() {
    fullscreenFallback.textContent = 'Fullscreen is unavailable or was denied by the browser. You can try again or continue; tab changes will still be monitored when possible.';
    fullscreenFallback.classList.remove('hidden');
    continueWithoutFullscreenButton.classList.remove('hidden');
    beginExamButton.disabled = false;
    beginExamButton.textContent = 'Try fullscreen again';
}

function activateExam() {
    state.started = true;
    welcome.classList.add('hidden');
    examContent.classList.remove('hidden');
    clearMessage();
    renderQuestion();
    questionText.focus();
}

async function beginExam() {
    if (!state.questions.length || state.started) {
        return;
    }

    beginExamButton.disabled = true;

    if (document.fullscreenElement) {
        activateExam();
        return;
    }

    if (!document.documentElement.requestFullscreen) {
        showFullscreenFallback();
        return;
    }

    try {
        await document.documentElement.requestFullscreen();
        if (document.fullscreenElement) {
            activateExam();
        } else {
            showFullscreenFallback();
        }
    } catch (error) {
        showFullscreenFallback();
    }
}

async function loadQuizQuestions() {
    if (!state.attemptId) {
        window.location.replace('index.html');
        return;
    }

    candidateName.textContent = attemptData.playerName || 'Candidate';
    categoryBadge.textContent = attemptData.category || 'Assessment';
    difficultyBadge.textContent = attemptData.difficulty || '-';
    activeCategoryBadge.textContent = attemptData.category || 'Assessment';
    beginExamButton.disabled = true;

    try {
        const response = await fetch(`/api/quiz/${state.attemptId}/questions`);
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || 'Unable to load the assessment questions.');
        }

        state.questions = data;
        questionTotal.textContent = String(state.questions.length);
        progressWrap.setAttribute('aria-valuemax', String(state.questions.length));
        beginExamButton.disabled = false;
    } catch (error) {
        showMessage(error.message || 'Unable to load assessment questions.');
        beginExamButton.textContent = 'Return to setup';
        beginExamButton.disabled = false;
        beginExamButton.addEventListener('click', () => window.location.replace('index.html'), { once: true });
    }
}

function renderQuestion(startTimer = true) {
    const totalQuestions = state.questions.length;
    const currentQuestion = state.questions[state.currentIndex];

    if (!currentQuestion) {
        return;
    }

    questionCounter.textContent = `Question ${state.currentIndex + 1} of ${totalQuestions}`;
    questionText.textContent = currentQuestion.questionText;
    progressBar.style.width = `${((state.currentIndex + 1) / totalQuestions) * 100}%`;
    progressWrap.setAttribute('aria-valuenow', String(state.currentIndex + 1));
    answerOptions.replaceChildren();

    const questionExpired = state.expiredQuestionIds.has(currentQuestion.id);
    currentQuestion.options.forEach((option, optionIndex) => {
        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'option-button';
        button.textContent = `${String.fromCharCode(65 + optionIndex)}.  ${option}`;
        button.setAttribute('aria-pressed', String(state.answers[currentQuestion.id] === option));
        button.disabled = questionExpired;

        if (state.answers[currentQuestion.id] === option) {
            button.classList.add('selected');
        }

        button.addEventListener('click', () => {
            state.answers[currentQuestion.id] = option;
            clearMessage();
            renderQuestion(false);
        });

        answerOptions.appendChild(button);
    });

    prevButton.disabled = state.currentIndex === 0;
    const isLastQuestion = state.currentIndex === totalQuestions - 1;
    nextButton.classList.toggle('hidden', isLastQuestion);
    submitButton.classList.toggle('hidden', !isLastQuestion);

    if (startTimer) {
        startQuestionTimer();
    }
}

function stopQuestionTimer() {
    const timerWasRunning = Boolean(state.timerInterval);
    if (state.timerInterval) {
        window.clearInterval(state.timerInterval);
        state.timerInterval = null;
    }

    const currentQuestion = state.questions[state.currentIndex];
    if (timerWasRunning && currentQuestion && !state.expiredQuestionIds.has(currentQuestion.id)) {
        state.timeRemainingByQuestion[currentQuestion.id] = state.timeRemaining;
    }
}

function updateTimerDisplay() {
    const seconds = Math.max(0, state.timeRemaining);
    questionTimer.textContent = `00:${String(seconds).padStart(2, '0')}`;
    timerBar.style.width = `${(seconds / 30) * 100}%`;
    timerBox.classList.toggle('timer-low', seconds <= 10 && seconds > 0);
    timerBox.classList.toggle('timer-expired', seconds === 0);
    timerBox.setAttribute('aria-valuetext', `${seconds} seconds remaining`);
}

function startQuestionTimer() {
    stopQuestionTimer();
    const currentQuestion = state.questions[state.currentIndex];
    if (!currentQuestion) {
        return;
    }

    state.timeRemaining = state.timeRemainingByQuestion[currentQuestion.id] ?? 30;
    updateTimerDisplay();
    if (state.expiredQuestionIds.has(currentQuestion.id)) {
        return;
    }

    state.timerInterval = window.setInterval(() => {
        state.timeRemaining = Math.max(0, state.timeRemaining - 1);
        state.timeRemainingByQuestion[currentQuestion.id] = state.timeRemaining;
        updateTimerDisplay();

        if (state.timeRemaining === 0) {
            window.clearInterval(state.timerInterval);
            state.timerInterval = null;
            state.expiredQuestionIds.add(currentQuestion.id);
            showMessage('Time is up for this question. Moving to the next question.', 'warning');
            if (state.currentIndex === state.questions.length - 1) {
                showMessage('Time is up. Submitting your assessment now.', 'warning');
                submitAssessment(true, 'timer');
            } else {
                state.currentIndex += 1;
                renderQuestion();
            }
        }
    }, 1000);
}

function countFocusViolation() {
    if (state.isSubmitting) {
        return;
    }

    state.violations += 1;
    violationCount.textContent = `Interruptions: ${state.violations} / 2`;

    if (state.violations === 1) {
        showMessage('Focus notice 1 of 2: returning to this assessment was recorded. A second interruption submits your current answers.', 'warning');
        resumeFullscreenButton.classList.toggle('hidden', Boolean(document.fullscreenElement) || !document.documentElement.requestFullscreen);
        return;
    }

    showMessage('Second interruption recorded. Submitting your current answers now.', 'warning');
    submitAssessment(true);
}

async function submitAssessment(automatic = false, cause = 'interruption') {
    if (state.isSubmitting || !state.attemptId) {
        return;
    }

    if (!automatic) {
        const answeredCount = Object.values(state.answers).filter(Boolean).length;
        const unansweredCount = state.questions.length - answeredCount;
        if (unansweredCount > 0 && !window.confirm(`You have ${unansweredCount} unanswered question(s). Submit this assessment?`)) {
            return;
        }
    }

    state.isSubmitting = true;
    stopQuestionTimer();
    submitButton.disabled = true;
    nextButton.disabled = true;
    prevButton.disabled = true;
    skipButton.disabled = true;
    submitButton.textContent = automatic && cause === 'timer' ? 'Time expired. Submitting...' : 'Submitting...';

    try {
        const response = await fetch(`/api/quiz/${state.attemptId}/submit`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ answers: state.answers }),
            keepalive: automatic
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || 'Assessment submission failed.');
        }

        sessionStorage.setItem('quizResults', JSON.stringify(data));
        if (document.fullscreenElement && document.exitFullscreen) {
            await document.exitFullscreen().catch(() => {});
        }
        window.location.href = 'result.html';
    } catch (error) {
        state.isSubmitting = false;
        submitButton.disabled = false;
        nextButton.disabled = false;
        prevButton.disabled = state.currentIndex === 0;
        skipButton.disabled = false;
        submitButton.textContent = 'Submit assessment';
        showMessage(error.message || 'Something went wrong while submitting the assessment.');
    }
}

beginExamButton.addEventListener('click', beginExam);
continueWithoutFullscreenButton.addEventListener('click', activateExam);

resumeFullscreenButton.addEventListener('click', async () => {
    try {
        await document.documentElement.requestFullscreen();
        resumeFullscreenButton.classList.add('hidden');
        clearMessage();
    } catch (error) {
        showMessage('Fullscreen could not be restored. The assessment remains active and interruptions are still monitored.', 'warning');
    }
});

prevButton.addEventListener('click', () => {
    if (state.currentIndex > 0) {
        stopQuestionTimer();
        state.currentIndex -= 1;
        clearMessage();
        renderQuestion();
    }
});

nextButton.addEventListener('click', () => {
    const currentQuestion = state.questions[state.currentIndex];
    if (!currentQuestion) {
        return;
    }

    if (!state.answers[currentQuestion.id]) {
        showMessage('Select an answer or skip this question before continuing.');
        return;
    }

    if (state.currentIndex < state.questions.length - 1) {
        stopQuestionTimer();
        state.currentIndex += 1;
        clearMessage();
        renderQuestion();
    }
});

skipButton.addEventListener('click', () => {
    const currentQuestion = state.questions[state.currentIndex];
    if (!currentQuestion) {
        return;
    }

    delete state.answers[currentQuestion.id];
    if (state.currentIndex < state.questions.length - 1) {
        stopQuestionTimer();
        state.currentIndex += 1;
        clearMessage();
        renderQuestion();
    } else {
        clearMessage();
    }
});

submitButton.addEventListener('click', () => submitAssessment(false));

document.addEventListener('visibilitychange', () => {
    if (document.visibilityState === 'hidden') {
        countFocusViolation();
    }
});

document.addEventListener('fullscreenchange', () => {
    if (state.started && !document.fullscreenElement && document.visibilityState !== 'hidden') {
        countFocusViolation();
    } else if (document.fullscreenElement) {
        resumeFullscreenButton.classList.add('hidden');
    }
});

loadQuizQuestions();
