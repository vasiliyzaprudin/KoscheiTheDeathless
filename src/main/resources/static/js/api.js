/**
 * Запрашивает геометрию с сервера.
 * @param {number} width - Ширина выработки
 * @param {number} wallHeight - Высота стенок
 * @returns {Promise<Object>} Модель DrawingModel
 */
export async function fetchGeometry(width, wallHeight) {
    const url = `/api/drawing/geometry?width=${width}&wallHeight=${wallHeight}`;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(`Ошибка сервера: ${response.status} ${response.statusText}`);
    }

    return response.json();
}