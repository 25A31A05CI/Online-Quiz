const categorySelect = document.getElementById('category');
const difficultySelect = document.getElementById('difficulty');
const form = document.getElementById('quizForm');
const messageBox = document.getElementById('messageBox');

function showMessage(message, type = 'error') {
    messageBox.textContent = message;
    messageBox.classList.remove('hidden', 'error', 'success');
    messageBox.classList.add(type);
}

async function loadCategories() {
    try {
        const response = await fetch('/api/quiz/categories');

        if (!response.ok) {
            throw new Error('Could not load quiz categories.');
        }

        const categories = await response.json();
        categorySelect.innerHTML = '<option value="">Choose a category</option>';

        categories.forEach((item) => {
            const option = document.createElement('option');
            option.value = item.category;
            option.textContent = item.label;
            categorySelect.appendChild(option);
        });
    } catch (error) {
        showMessage(error.message || 'Unable to load categories. Please refresh the page.');
    }
}

form.addEventListener('submit', async (event) => {
    event.preventDefault();
    const playerName = document.getElementById('playerName').value.trim();
    const category = categorySelect.value;
    const difficulty = difficultySelect.value;

    if (!playerName || playerName.length < 2) {
        showMessage('Please enter a valid name with at least 2 characters.');
        return;
    }

    if (!category) {
        showMessage('Please select a quiz category.');
        return;
    }

    if (!difficulty) {
        showMessage('Please select a difficulty level.');
        return;
    }

    const startButton = document.getElementById('startButton');
    startButton.disabled = true;
    startButton.textContent = 'Starting...';
    showMessage('Starting your quiz...', 'success');

    try {
        const response = await fetch('/api/quiz/start', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                playerName,
                category,
                difficulty
            })
        });

        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || 'Unable to start the quiz.');
        }

        sessionStorage.setItem('quizAttempt', JSON.stringify({
            attemptId: data.attemptId,
            playerName: data.playerName,
            category: data.category,
            difficulty: data.difficulty,
            totalQuestions: data.totalQuestions
        }));

        window.location.href = 'quiz.html';
    } catch (error) {
        showMessage(error.message || 'The quiz could not start. Please try again.');
        startButton.disabled = false;
        startButton.textContent = 'Start Quiz';
    }
});

document.addEventListener('DOMContentLoaded', loadCategories);
