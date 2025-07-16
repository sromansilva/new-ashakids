// padre-mensajes.js
// Funcionalidad de mensajería para el padre

document.addEventListener('DOMContentLoaded', function () {
    const listaTerapeutas = document.getElementById('listaTerapeutas');
    const buscadorTerapeutas = document.getElementById('buscadorTerapeutas');
    const chatMensajes = document.getElementById('chatMensajes');
    const nombreTerapeutaChat = document.getElementById('nombreTerapeutaChat');
    const formEnviarMensaje = document.getElementById('formEnviarMensaje');
    const inputMensaje = document.getElementById('inputMensaje');
    const idTerapeutaSeleccionado = document.getElementById('idTerapeutaSeleccionado');
    const btnRefrescarChat = document.getElementById('btnRefrescarChat');

    let terapeutaActual = null;
    let terapeutasData = [];
    let paginaActual = 0;
    let totalPaginas = 1;
    const pageSize = 30;

    // Cargar lista de terapeutas
    function cargarTerapeutas(busqueda = '') {
        fetch(`/api/padre/mensajes/terapeutas?busqueda=${encodeURIComponent(busqueda)}`)
            .then(r => r.json())
            .then(data => {
                terapeutasData = data;
                renderTerapeutas();
            });
    }

    // Renderizar lista de terapeutas
    function renderTerapeutas() {
        listaTerapeutas.innerHTML = '';
        if (!terapeutasData.length) {
            listaTerapeutas.innerHTML = '<div class="text-center text-muted p-3">No hay terapeutas</div>';
            return;
        }
        terapeutasData.forEach(terapeuta => {
            const btn = document.createElement('button');
            btn.className = 'list-group-item list-group-item-action d-flex justify-content-between align-items-center terapeuta-item';
            // Mostrar foto o ícono
            const fotoHtml = terapeuta.foto ? `<img src='${terapeuta.foto}' class='foto-lista' alt='Foto'>` : "<i class='fas fa-user-md me-2'></i>";
            btn.innerHTML = `<span>${fotoHtml} ${terapeuta.nombre}</span>`;
            if (terapeuta.noLeidos > 0) {
                btn.innerHTML += `<span class='badge bg-danger rounded-pill'>${terapeuta.noLeidos}</span>`;
            }
            btn.onclick = () => seleccionarTerapeuta(terapeuta);
            if (terapeutaActual && terapeutaActual.idTerapeuta === terapeuta.idTerapeuta) {
                btn.classList.add('active');
            }
            listaTerapeutas.appendChild(btn);
        });
    }

    // Seleccionar un terapeuta y cargar mensajes
    function seleccionarTerapeuta(terapeuta) {
        terapeutaActual = terapeuta;
        idTerapeutaSeleccionado.value = terapeuta.idTerapeuta;
        nombreTerapeutaChat.textContent = terapeuta.nombre;
        // Mostrar foto en el encabezado
        const fotoElem = document.getElementById('fotoTerapeutaChat');
        if (fotoElem) {
            fotoElem.src = terapeuta.foto ? terapeuta.foto : 'https://cdn-icons-png.flaticon.com/512/1077/1077114.png';
        }
        paginaActual = 0;
        cargarMensajes();
        marcarLeidos();
        renderTerapeutas();
    }

    // Cargar mensajes del chat
    function cargarMensajes() {
        if (!terapeutaActual) return;
        fetch(`/api/padre/mensajes/conversacion?idTerapeuta=${terapeutaActual.idTerapeuta}&pagina=${paginaActual}&size=${pageSize}`)
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
            div.className = 'mensaje mb-2 ' + (m.emisor === 'padre' ? 'mensaje-propio' : 'mensaje-otro');
            div.innerHTML = `<div class='mensaje-cuerpo'>${escapeHtml(m.mensaje)}</div><div class='mensaje-fecha'>${formatearFecha(m.fechaEnvio)}</div>`;
            chatMensajes.appendChild(div);
        });
    }

    // Enviar mensaje
    formEnviarMensaje.addEventListener('submit', function (e) {
        e.preventDefault();
        if (!terapeutaActual) return;
        const texto = inputMensaje.value.trim();
        if (!texto) return;
        fetch('/api/padre/mensajes/enviar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ idTerapeuta: terapeutaActual.idTerapeuta, mensaje: texto })
        })
            .then(r => r.json())
            .then(msg => {
                inputMensaje.value = '';
                cargarMensajes();
                cargarTerapeutas();
            });
    });

    // Marcar mensajes como leídos
    function marcarLeidos() {
        if (!terapeutaActual) return;
        fetch('/api/padre/mensajes/marcar-leido', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ idTerapeuta: terapeutaActual.idTerapeuta })
        }).then(() => cargarTerapeutas());
    }

    // Buscar terapeutas
    buscadorTerapeutas.addEventListener('input', function () {
        cargarTerapeutas(this.value);
    });

    // Refrescar chat
    btnRefrescarChat.addEventListener('click', function () {
        cargarMensajes();
        cargarTerapeutas();
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
    cargarTerapeutas();
}); 