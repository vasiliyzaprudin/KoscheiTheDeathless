import { fetchGeometry } from './api.js';
import { drawModel, resetSelection, resetZoom, getCurrentSvg } from './renderer.js';
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

const btnGeneratePdf = document.getElementById('btn-generate-pdf');

btnGeneratePdf.addEventListener('click', async () => {
    const width = parseFloat(document.getElementById('params').querySelector('[name="width"]').value);
    const wallHeight = parseFloat(document.getElementById('params').querySelector('[name="wallHeight"]').value);
    const description = document.getElementById('description').value;
    const svgContent = getCurrentSvg(); // Получаем SVG-код из DOM

    if (!svgContent) {
        alert('Сначала постройте чертеж!');
        return;
    }

    const payload = {
        width: width,
        wallHeight: wallHeight,
        description: description,
        svgContent: svgContent
    };

    try {
        // Отправляем POST-запрос на сервер
        const response = await fetch('/api/report/pdf', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error('Ошибка при генерации PDF на сервере');
        }

        // Получаем PDF как Blob (бинарные данные)
        const blob = await response.blob();
        // Создаем временную ссылку для скачивания
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'passport.pdf';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
    } catch (error) {
        console.error(error);
        alert('Не удалось сформировать отчет: ' + error.message);
    }
});