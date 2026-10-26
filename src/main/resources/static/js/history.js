const historyRows = document.getElementById('historyRows');
const historyMessage = document.getElementById('historyMessage');
const recordCount = document.getElementById('recordCount');
const refreshHistoryButton = document.getElementById('refreshHistoryButton');

function showHistoryError(message) {
    historyMessage.textContent = message;
    historyMessage.classList.remove('hidden', 'success', 'warning');
    historyMessage.classList.add('error');
}

function formatPlayedAt(value) {
    let date;
    if (Array.isArray(value)) {
        date = new Date(value[0], value[1] - 1, value[2], value[3] || 0, value[4] || 0, value[5] || 0);
    } else {
        date = new Date(value);
    }

    if (Number.isNaN(date.getTime())) {
        return String(value || 'Unknown');
    }

    return new Intl.DateTimeFormat(undefined, {
        dateStyle: 'medium',
        timeStyle: 'short'
    }).format(date);
}

function renderHistory(entries) {
    historyRows.replaceChildren();
    recordCount.textContent = `${entries.length} ${entries.length === 1 ? 'record' : 'records'}`;

    if (entries.length === 0) {
        const row = document.createElement('tr');
        const cell = document.createElement('td');
        cell.colSpan = 3;
        cell.className = 'empty-row';
        cell.textContent = 'No completed assessments yet.';
        row.appendChild(cell);
        historyRows.appendChild(row);
        return;
    }

    entries.forEach((entry) => {
        const row = document.createElement('tr');
        const nameCell = document.createElement('td');
        const scoreCell = document.createElement('td');
        const timeCell = document.createElement('td');

        nameCell.textContent = entry.candidateName;
        scoreCell.textContent = `${entry.score} / 15`;
        timeCell.textContent = formatPlayedAt(entry.time);

        row.append(nameCell, scoreCell, timeCell);
        historyRows.appendChild(row);
    });
}

async function loadHistory() {
    refreshHistoryButton.disabled = true;
    historyMessage.classList.add('hidden');
    recordCount.textContent = 'Loading records...';

    try {
        const response = await fetch('/api/quiz/history');
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || 'Unable to load the attempt history.');
        }
        renderHistory(data);
    } catch (error) {
        historyRows.replaceChildren();
        recordCount.textContent = 'History unavailable';
        showHistoryError(error.message || 'Unable to load the attempt history.');
    } finally {
        refreshHistoryButton.disabled = false;
    }
}

refreshHistoryButton.addEventListener('click', loadHistory);
loadHistory();