// terapeuta-mensajes.js
// Funcionalidad de mensajería para el terapeuta

document.addEventListener('DOMContentLoaded', function () {
    const listaPadres = document.getElementById('listaPadres');
    const buscadorPadres = document.getElementById('buscadorPadres');
    const chatMensajes = document.getElementById('chatMensajes');
    const nombrePadreChat = document.getElementById('nombrePadreChat');
    const formEnviarMensaje = document.getElementById('formEnviarMensaje');
    const inputMensaje = document.getElementById('inputMensaje');
    const idPadreSeleccionado = document.getElementById('idPadreSeleccionado');
    const btnRefrescarChat = document.getElementById('btnRefrescarChat');

    let padreActual = null;
    let padresData = [];
    let paginaActual = 0;
    let totalPaginas = 1;
    const pageSize = 30;

    // Cargar lista de padres
    function cargarPadres(busqueda = '') {
        fetch(`/api/terapeuta/mensajes/padres?busqueda=${encodeURIComponent(busqueda)}`)
            .then(r => r.json())
            .then(data => {
                padresData = data;
                renderPadres();
            });
    }

    // Renderizar lista de padres
    function renderPadres() {
        listaPadres.innerHTML = '';
        if (!padresData.length) {
            listaPadres.innerHTML = '<div class="text-center text-muted p-3">No hay conversaciones</div>';
            return;
        }
        padresData.forEach(padre => {
            const btn = document.createElement('button');
            btn.className = 'list-group-item list-group-item-action d-flex justify-content-between align-items-center padre-item';
            // Mostrar foto pequeña usando el endpoint
            const fotoUrl = `/terapeuta/foto-padre/${padre.idPadre}`;
            const fotoHtml = `<img src='${fotoUrl}' class='foto-lista' alt='Foto' onerror=\"this.onerror=null;this.src='https://cdn-icons-png.flaticon.com/512/1077/1077012.png';\">`;
            btn.innerHTML = `<span>${fotoHtml} ${padre.nombre}</span>`;
            if (padre.noLeidos > 0) {
                btn.innerHTML += `<span class='badge bg-danger rounded-pill'>${padre.noLeidos}</span>`;
            }
            btn.onclick = () => seleccionarPadre(padre);
            if (padreActual && padreActual.idPadre === padre.idPadre) {
                btn.classList.add('active');
            }
            listaPadres.appendChild(btn);
        });
    }

    // Seleccionar un padre y cargar mensajes
    function seleccionarPadre(padre) {
        padreActual = padre;
        idPadreSeleccionado.value = padre.idPadre;
        nombrePadreChat.textContent = padre.nombre;
        // Mostrar foto en el encabezado
        const fotoElem = document.getElementById('fotoPadreChat');
        if (fotoElem) {
            // Usar el nuevo endpoint para la foto de perfil del padre
            fotoElem.onerror = function() {
                this.onerror = null;
                this.src = 'https://cdn-icons-png.flaticon.com/512/1077/1077012.png';
            };
            fotoElem.src = '/terapeuta/foto-padre/' + padre.idPadre;
        }
        paginaActual = 0;
        cargarMensajes();
        marcarLeidos();
        renderPadres();
    }

    // Cargar mensajes del chat
    function cargarMensajes() {
        if (!padreActual) return;
        fetch(`/api/terapeuta/mensajes/conversacion?idPadre=${padreActual.idPadre}&pagina=${paginaActual}&size=${pageSize}`)
            .then(r => r.json())
            .then(data => {
                renderMensajes(data.mensajes);
                totalPaginas = data.totalPaginas;
                setTimeout(() => scrollToBottom(), 100);
            });
    }

    // Renderizar mensajes en el chat
    function renderMensajes(mensajes) {
        chatMensajes.innerHTML = '';
        if (!mensajes.length) {
            chatMensajes.innerHTML = '<div class="text-center text-muted">No hay mensajes</div>';
            return;
        }
        mensajes.forEach(m => {
            const div = document.createElement('div');
            div.className = 'mensaje mb-2 ' + (m.emisor === 'terapeuta' ? 'mensaje-propio' : 'mensaje-otro');
            div.innerHTML = `<div class='mensaje-cuerpo'>${escapeHtml(m.mensaje)}</div><div class='mensaje-fecha'>${formatearFecha(m.fechaEnvio)}</div>`;
            chatMensajes.appendChild(div);
        });
    }

    // Enviar mensaje
    formEnviarMensaje.addEventListener('submit', function (e) {
        e.preventDefault();
        if (!padreActual) return;
        const texto = inputMensaje.value.trim();
        if (!texto) return;
        fetch('/api/terapeuta/mensajes/enviar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ idPadre: padreActual.idPadre, mensaje: texto })
        })
            .then(r => r.json())
            .then(msg => {
                inputMensaje.value = '';
                cargarMensajes();
                cargarPadres();
            });
    });

    // Marcar mensajes como leídos
    function marcarLeidos() {
        if (!padreActual) return;
        fetch('/api/terapeuta/mensajes/marcar-leido', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ idPadre: padreActual.idPadre })
        }).then(() => cargarPadres());
    }

    // Buscar padres
    buscadorPadres.addEventListener('input', function () {
        cargarPadres(this.value);
    });

    // Refrescar chat
    btnRefrescarChat.addEventListener('click', function () {
        cargarMensajes();
        cargarPadres();
    });

    // Scroll automático al final
    function scrollToBottom() {
        chatMensajes.scrollTop = chatMensajes.scrollHeight;
    }

    // Utilidades
    function escapeHtml(text) {
        const map = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' };
        return text.replace(/[&<>"']/g, m => map[m]);
    }
    function formatearFecha(fechaIso) {
        if (!fechaIso) return '';
        const d = new Date(fechaIso);
        return d.toLocaleString([], { hour: '2-digit', minute: '2-digit', day: '2-digit', month: '2-digit', year: '2-digit' });
    }

    // Inicializar
    cargarPadres();
}); 