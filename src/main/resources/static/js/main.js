import { fetchGeometry } from './api.js';
import { drawModel, resetSelection, resetZoom } from './renderer.js'; // ← добавили resetZoom
import { showElementInfo, resetInfoPanel } from './infoPanel.js';

const container = document.getElementById('svg-container');
const form = document.getElementById('params');
const btnResetZoom = document.getElementById('btn-reset-zoom'); // ← добавили

// Обработка формы
form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const fd = new FormData(e.target);
    const width = parseFloat(fd.get('width'));
    const wallHeight = parseFloat(fd.get('wallHeight'));

    try {
        const model = await fetchGeometry(width, wallHeight);
        drawModel(model, container, showElementInfo);
        resetInfoPanel();
    } catch (error) {
        console.error(error);
        alert('Не удалось построить сечение: ' + error.message);
    }
});

// Обработка кнопки сброса зума
btnResetZoom.addEventListener('click', () => {
    resetZoom();
});

// Глобальный сброс выделения
document.addEventListener('click', (e) => {
    const isDrawingElement = e.target.closest('line, path');
    if (!isDrawingElement) {
        resetSelection();
        resetInfoPanel();
    }
});