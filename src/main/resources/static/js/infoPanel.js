const infoPanel = document.getElementById('element-info');

export function resetInfoPanel() {
    infoPanel.innerHTML = '<p style="color: #888; font-style: italic;">Кликните на любой элемент чертежа...</p>';
}

/**
 * Отображает информацию о примитиве.
 * @param {Object} prim - Объект примитива из модели
 */
export function showElementInfo(prim) {
    let html = '';

    html += `<div class="info-row"><span class="info-label">Роль:</span> <span class="info-value">${prim.role}</span></div>`;
    html += `<div class="info-row"><span class="info-label">ID:</span> <span style="font-size: 0.8em; color: #666;">${prim.id}</span></div>`;

    if (prim.start && prim.end) {
        const dx = prim.end.x - prim.start.x;
        const dy = prim.end.y - prim.start.y;
        const length = Math.sqrt(dx * dx + dy * dy);

        html += `<div class="info-row"><span class="info-label">Тип:</span> <span class="info-value">Линия</span></div>`;
        html += `<div class="info-row"><span class="info-label">Начало:</span> <span class="info-value">(${prim.start.x}, ${prim.start.y})</span></div>`;
        html += `<div class="info-row"><span class="info-label">Конец:</span> <span class="info-value">(${prim.end.x}, ${prim.end.y})</span></div>`;
        html += `<div class="info-row"><span class="info-label">Длина:</span> <span class="info-value">${length.toFixed(2)}</span></div>`;

    } else if (prim.center && prim.radius !== undefined) {
        html += `<div class="info-row"><span class="info-label">Тип:</span> <span class="info-value">Дуга</span></div>`;
        html += `<div class="info-row"><span class="info-label">Центр:</span> <span class="info-value">(${prim.center.x}, ${prim.center.y})</span></div>`;
        html += `<div class="info-row"><span class="info-label">Радиус:</span> <span class="info-value">${prim.radius}</span></div>`;
        html += `<div class="info-row"><span class="info-label">Углы:</span> <span class="info-value">${prim.startAngleDeg}° — ${prim.endAngleDeg}°</span></div>`;
    }

    infoPanel.innerHTML = html;
}