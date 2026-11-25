const API = {
    async request(url, options = {}) {
        const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };

        const token = localStorage.getItem('jwt');
        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }

        const response = await fetch(url, {
            ...options,
            headers
        });

        let data = null;
        const text = await response.text();
        if (text) {
            try { data = JSON.parse(text); } catch { data = text; }
        }

        if (!response.ok) {
            const error = new Error(`HTTP ${response.status}`);
            error.status = response.status;
            error.data = data;
            throw error;
        }
        return data;
    },
    get(url) { return this.request(url); },
    post(url, body) { return this.request(url, { method: 'POST', body: JSON.stringify(body) }); },
    put(url, body) { return this.request(url, { method: 'PUT', body: JSON.stringify(body) }); },
    delete(url) { return this.request(url, { method: 'DELETE' }); }
};