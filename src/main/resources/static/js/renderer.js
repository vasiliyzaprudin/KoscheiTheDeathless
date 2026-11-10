const SVG_NS = "http://www.w3.org/2000/svg";

let selectedElement = null;
let currentScale = 1;
let currentTranslate = { x: 0, y: 0 };

export function resetSelection() {
    if (selectedElement) {
        selectedElement.classList.remove('highlight');
        selectedElement = null;
    }
}

export function resetZoom() {
    currentScale = 1;
    currentTranslate = { x: 0, y: 0 };
    const g = document.querySelector('#svg-container svg g');
    if (g) {
        g.setAttribute('transform', `translate(0, 0) scale(1)`);
    }
}

export function drawModel(model, container, onElementClick) {
    container.innerHTML = '';
    resetSelection();
    currentScale = 1;
    currentTranslate = { x: 0, y: 0 };

    const canvasWidth = 800;
    const canvasHeight = 600;
    const svg = document.createElementNS(SVG_NS, 'svg');
    svg.setAttribute('width', canvasWidth);
    svg.setAttribute('height', canvasHeight);
    svg.style.cursor = 'default';

    const g = document.createElementNS(SVG_NS, 'g');
    svg.appendChild(g);

    const padding = 40;
    const cw = canvasWidth - padding * 2;
    const ch = canvasHeight - padding * 2;
    const modelW = model.bounds.maxX - model.bounds.minX;
    const modelH = model.bounds.maxY - model.bounds.minY;
    const baseScale = Math.min(cw / modelW, ch / modelH);

    const baseOffsetX = padding + (cw - modelW * baseScale) / 2 - model.bounds.minX * baseScale;
    const baseOffsetY = padding + (ch - modelH * baseScale) / 2 - model.bounds.minY * baseScale;

    function toSvg(p) {
        return {
            x: baseOffsetX + p.x * baseScale,
            y: canvasHeight - (baseOffsetY + p.y * baseScale)
        };
    }

    for (const prim of model.primitives) {
        let element;

        if (prim.start && prim.end) {
            element = document.createElementNS(SVG_NS, 'line');
            const s = toSvg(prim.start);
            const e = toSvg(prim.end);
            element.setAttribute('x1', s.x);
            element.setAttribute('y1', s.y);
            element.setAttribute('x2', e.x);
            element.setAttribute('y2', e.y);
        } else if (prim.center && prim.radius !== undefined) {
            const c = toSvg(prim.center);
            const r = prim.radius * baseScale;
            const startRad = (prim.startAngleDeg * Math.PI) / 180;
            const endRad = (prim.endAngleDeg * Math.PI) / 180;
            const spModel = { x: prim.center.x + prim.radius * Math.cos(startRad), y: prim.center.y + prim.radius * Math.sin(startRad) };
            const epModel = { x: prim.center.x + prim.radius * Math.cos(endRad), y: prim.center.y + prim.radius * Math.sin(endRad) };
            const sp = toSvg(spModel);
            const ep = toSvg(epModel);

            element = document.createElementNS(SVG_NS, 'path');
            const d = `M ${sp.x} ${sp.y} A ${r} ${r} 0 0 0 ${ep.x} ${ep.y}`;
            element.setAttribute('d', d);
            element.setAttribute('fill', 'none');
        }

        if (element) {
            element.setAttribute('stroke', prim.style.stroke || '#000');
            element.setAttribute('stroke-width', prim.style.strokeWidth || 1);
            if (prim.style.strokeDasharray) {
                element.setAttribute('stroke-dasharray', prim.style.strokeDasharray);
            }

            // Hover
            element.addEventListener('mouseenter', () => element.classList.add('highlight'));
            element.addEventListener('mouseleave', () => {
                if (element !== selectedElement) element.classList.remove('highlight');
            });

            // Click
            element.addEventListener('click', (event) => {
                event.stopPropagation(); // Останавливаем всплытие до document
                if (selectedElement) selectedElement.classList.remove('highlight');
                selectedElement = element;
                element.classList.add('highlight');
                onElementClick(prim);
            });

            g.appendChild(element);
        }
    }

    container.appendChild(svg);
    setupZoom(svg, g);
}

function setupZoom(svg, g) {
    svg.addEventListener('wheel', (e) => {
        e.preventDefault();
        const zoomFactor = 1.1;
        const direction = e.deltaY < 0 ? 1 : -1;
        const newScale = direction > 0 ? currentScale * zoomFactor : currentScale / zoomFactor;
        if (newScale < 0.5 || newScale > 5) return;

        const rect = svg.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;

        currentTranslate.x = mouseX - (mouseX - currentTranslate.x) * (newScale / currentScale);
        currentTranslate.y = mouseY - (mouseY - currentTranslate.y) * (newScale / currentScale);
        currentScale = newScale;

        g.setAttribute('transform', `translate(${currentTranslate.x}, ${currentTranslate.y}) scale(${currentScale})`);
    }, { passive: false });
}