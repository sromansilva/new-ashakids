// Generar slots de tiempo
function generarTimeSlots() {
    const container = document.getElementById('timeSlots');
    container.innerHTML = ''; // Limpiar slots existentes
    
    const horas = ['08:00', '08:30', '09:00', '09:30', '10:00', '10:30', '11:00', '11:30',
                  '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30',
                  '16:00', '16:30', '17:00', '17:30', '18:00'];
    
    const fechaSeleccionada = document.getElementById('fecha').value;
    const hoy = new Date();
    const esHoy = fechaSeleccionada === hoy.toISOString().split('T')[0];
    const horaActual = hoy.getHours();
    const minutosActuales = hoy.getMinutes();
    
    horas.forEach(hora => {
        const slot = document.createElement('div');
        slot.className = 'col-md-2 col-sm-3 col-4';
        
        // Convertir hora a formato AM/PM
        const [hours, minutes] = hora.split(':').map(Number);
        const horaAMPM = formatearHoraAMPM(hours, minutes);
        
        // Verificar si el horario está disponible
        let estaDisponible = true;
        let claseAdicional = '';
        
        if (esHoy) {
            const [slotHours, slotMinutes] = hora.split(':').map(Number);
            if (slotHours < horaActual || (slotHours === horaActual && slotMinutes <= minutosActuales)) {
                estaDisponible = false;
                claseAdicional = 'disabled';
            }
        }
        
        // Separar hora y período para mejor formato
        const displayHours = hours === 0 ? 12 : (hours > 12 ? hours - 12 : hours);
        const displayMinutes = minutes.toString().padStart(2, '0');
        const period = hours >= 12 ? 'PM' : 'AM';
        const timeOfDay = hours < 12 ? 'Mañana' : 'Tarde';
        
        slot.innerHTML = `
            <div class="time-slot ${claseAdicional}" data-hora="${hora}" ${!estaDisponible ? 'style="opacity: 0.5; cursor: not-allowed;"' : ''}>
                <div class="time-hour">${displayHours}:${displayMinutes}</div>
                <div class="time-period">${period}</div>
                <div class="time-day">${timeOfDay}</div>
                ${!estaDisponible ? '<small class="text-muted">Pasado</small>' : ''}
            </div>
        `;
        container.appendChild(slot);
    });
}

// Función para formatear hora en AM/PM
function formatearHoraAMPM(hours, minutes) {
    const period = hours >= 12 ? 'PM' : 'AM';
    const displayHours = hours === 0 ? 12 : (hours > 12 ? hours - 12 : hours);
    const displayMinutes = minutes.toString().padStart(2, '0');
    return `${displayHours}:${displayMinutes} ${period}`;
}

// Selección de hora
document.addEventListener('click', function(e) {
    if (e.target.closest('.time-slot')) {
        const timeSlot = e.target.closest('.time-slot');
        
        // Verificar si está deshabilitado
        if (timeSlot.classList.contains('disabled')) {
            showError('Este horario ya no está disponible para hoy.');
            return;
        }
        
        // Remover selección previa
        document.querySelectorAll('.time-slot').forEach(slot => {
            slot.classList.remove('selected');
        });
        
        // Seleccionar nuevo slot
        timeSlot.classList.add('selected');
        document.getElementById('hora').value = timeSlot.dataset.hora;
    }
});

// Selección de modalidad
document.addEventListener('click', function(e) {
    if (e.target.closest('.modalidad-card')) {
        // Remover selección previa
        document.querySelectorAll('.modalidad-card').forEach(card => {
            card.classList.remove('selected');
        });
        
        // Seleccionar nueva modalidad
        e.target.closest('.modalidad-card').classList.add('selected');
        document.getElementById('modalidad').value = e.target.closest('.modalidad-card').dataset.modalidad;
        
        // Actualizar botón según modalidad
        actualizarBotonSegunModalidad();
    }
});

// Función para actualizar el botón según la modalidad
function actualizarBotonSegunModalidad() {
    const modalidad = document.getElementById('modalidad').value;
    const submitBtn = document.querySelector('button[type="submit"]');
    
    if (modalidad === 'Virtual') {
        submitBtn.innerHTML = '<i class="fas fa-video me-2"></i>Crear Reunión Zoom';
        submitBtn.className = 'btn btn-primary btn-lg';
    } else if (modalidad === 'Presencial') {
        submitBtn.innerHTML = '<i class="fas fa-calendar-check me-2"></i>Agendar Cita';
        submitBtn.className = 'btn btn-success btn-lg';
    } else {
        submitBtn.innerHTML = '<i class="fas fa-calendar-plus me-2"></i>Agendar Cita';
        submitBtn.className = 'btn btn-primary btn-lg';
    }
}

// Función para mostrar modal de éxito para citas presenciales
function mostrarModalCitaPresencial(detallesCita) {
    const modal = new bootstrap.Modal(document.getElementById('modalReunion'));
    const modalTitle = document.getElementById('modalReunionLabel');
    const modalBody = document.querySelector('#modalReunion .modal-body');
    const linkReunion = document.getElementById('linkReunion');
    
    modalTitle.innerHTML = '<i class="fas fa-check-circle me-2"></i>🎉 Cita Agendada';
    modalBody.innerHTML = `
        <div class="text-center">
            <div class="mb-3">
                <i class="fas fa-check-circle text-success" style="font-size: 3rem;"></i>
            </div>
            <h4 class="text-success mb-3">¡Cita Agendada Correctamente!</h4>
            <p class="fs-5 mb-4">Tu cita presencial ha sido agendada exitosamente.</p>
            <div class="alert alert-info">
                <strong>Detalles de la cita:</strong><br>
                ${detallesCita}
            </div>
            <div class="d-flex gap-2 justify-content-center mt-4">
                <a href="javascript:history.back()" class="btn btn-primary px-4 fw-bold">
                    <i class="fas fa-check me-2"></i>Entendido
                </a>
            </div>
        </div>
    `;
    
    // Ocultar el enlace de Zoom
    linkReunion.style.display = 'none';
    
    modal.show();
}

document.getElementById('form-agendar').addEventListener('submit', async function (e) {
    e.preventDefault();

    // Obtener todos los valores del formulario
    const idNino = document.getElementById('idNino').value;
    const idTerapeuta = document.getElementById('idTerapeuta').value;
    const fecha = document.getElementById('fecha').value;
    const hora = document.getElementById('hora').value;
    const duracion = document.getElementById('duracion').value;
    const modalidad = document.getElementById('modalidad').value;
    let tema = document.getElementById('tema').value;
    const nombreTutor = document.getElementById('nombre').value.trim();

    // Validaciones mejoradas
    // El niño ya está pre-seleccionado, no necesita validación

    if (!idTerapeuta) {
        showError('Por favor, selecciona un terapeuta.');
        document.getElementById('idTerapeuta').focus();
        return;
    }

    if (!fecha) {
        showError('Por favor, selecciona una fecha.');
        document.getElementById('fecha').focus();
        return;
    }

    if (!hora) {
        showError('Por favor, selecciona una hora para la cita.');
        return;
    }

    if (!duracion) {
        showError('Por favor, selecciona la duración de la sesión.');
        document.getElementById('duracion').focus();
        return;
    }

    if (!modalidad) {
        showError('Por favor, selecciona una modalidad.');
        return;
    }

    if (!tema) {
        showError('Por favor, selecciona un tema de la cita.');
        document.getElementById('tema').focus();
        return;
    }

    if (!nombreTutor) {
        showError('Por favor, ingresa el nombre del tutor.');
        document.getElementById('nombre').focus();
        return;
    }

    // Si seleccionó "Otro", validar que especifique el tema
    if (tema === 'Otro') {
        const temaOtro = document.getElementById('temaOtro').value.trim();
        if (!temaOtro) {
            showError('Por favor, especifica el tema de la terapia.');
            document.getElementById('temaOtro').focus();
            return;
        }
        // Usar el tema personalizado en lugar de "Otro"
        tema = temaOtro;
    }

    // Validar que la fecha no sea anterior a hoy
    const fechaSeleccionada = new Date(fecha + 'T' + hora);
    const hoy = new Date();
    
    if (fechaSeleccionada < hoy) {
        showError('No puedes agendar una cita en el pasado. Selecciona una fecha y hora futura.');
        return;
    }

    // Mostrar loading
    const submitBtn = this.querySelector('button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Procesando...';
    submitBtn.disabled = true;

    try {
        if (modalidad === 'Virtual') {
            // Crear reunión en Zoom
            const fechaISO = `${fecha}T${hora}:00`;

        const response = await fetch('http://localhost:3000/crear-reunion', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                topic: tema,
                start_time: fechaISO,
                duration: parseInt(duracion),
                    agenda: `Cita con ${nombre} - Modalidad: ${modalidad}`
            })
        });

        const data = await response.json();
        console.log("Respuesta del servidor:", data);

        if (response.ok) {
                // Guardar en la base de datos
                const citaGuardada = await guardarCitaEnBD({
                    idNino: idNino,
                    idTerapeuta: idTerapeuta,
                    fecha: fecha,
                    hora: hora,
                    modalidad: modalidad,
                    enlace_o_direccion: data.linkZoom || data.join_url,
                    nombreTutor: nombreTutor,
                    tema: tema
                });

                if (citaGuardada) {
            const modal = new bootstrap.Modal(document.getElementById('modalReunion'));
                    const modalTitle = document.getElementById('modalReunionLabel');
                    const modalBody = document.querySelector('#modalReunion .modal-body');
                    const linkReunion = document.getElementById('linkReunion');
                    
                    modalTitle.innerHTML = '<i class="fas fa-check-circle me-2"></i>🎉 Reunión Creada';
                    modalBody.innerHTML = `
                        <div class="text-center">
                            <div class="mb-3">
                                <i class="fas fa-check-circle text-success" style="font-size: 3rem;"></i>
                            </div>
                            <h4 class="text-success mb-3">¡Reunión Creada Correctamente!</h4>
                            <p class="fs-5 mb-4">Tu reunión de Zoom fue creada exitosamente.</p>
                            <div class="d-flex gap-2 justify-content-center flex-wrap">
                                <a href="#" id="linkReunion" class="btn btn-success px-4 fw-bold" target="_blank">
                                    <i class="fas fa-video me-2"></i>Unirme a la Reunión
                                </a>
                                <a href="javascript:history.back()" class="btn btn-success px-4 fw-bold">
                                    <i class="fas fa-home me-2"></i>Volver al Inicio
                                </a>
                            </div>
                        </div>
                    `;
                    
                    const joinUrl = data.linkZoom || data.join_url;
            document.getElementById('linkReunion').href = joinUrl;
                    console.log('🎥 Procesando reunión virtual...');
                    
                    // Ejecutar celebración ANTES de mostrar el modal
                    celebrarExito();
                    
            modal.show();
                    
                    // Mostrar mensaje de éxito
                    showSuccess('¡Cita virtual agendada exitosamente!');
                } else {
                    showError('Error al guardar la cita en la base de datos');
                }
            } else {
                showError(`Error al crear reunión: ${data.error || 'Verifica el servidor'}`);
            }
        } else {
            // Cita presencial - guardar en BD
            const citaGuardada = await guardarCitaEnBD({
                idNino: idNino,
                idTerapeuta: idTerapeuta,
                fecha: fecha,
                hora: hora,
                modalidad: modalidad,
                enlace_o_direccion: null, // No hay enlace para citas presenciales
                nombreTutor: nombreTutor,
                tema: tema
            });

            if (citaGuardada) {
                // Generar detalles de la cita presencial
                const detallesCita = `
                    <strong>Fecha:</strong> ${formatearFecha(fecha)}<br>
                    <strong>Hora:</strong> ${formatearHoraAMPM(parseInt(hora.split(':')[0]), parseInt(hora.split(':')[1]))}<br>
                    <strong>Duración:</strong> ${duracion} minutos<br>
                    <strong>Terapeuta:</strong> ${obtenerNombreTerapeuta(idTerapeuta)}<br>
                    <strong>Modalidad:</strong> ${modalidad}
                `;
                
                console.log('📅 Procesando cita presencial...');
                
                // Ejecutar celebración ANTES de mostrar el modal
                celebrarExito();
                
                // Mostrar modal con detalles
                mostrarModalCitaPresencial(detallesCita);
                
                // Mostrar mensaje de éxito
                showSuccess('¡Cita presencial agendada exitosamente!');
            } else {
                showError('Error al guardar la cita en la base de datos');
            }
        }

        this.reset();
        
        // Limpiar selecciones visuales
        document.querySelectorAll('.time-slot').forEach(slot => slot.classList.remove('selected'));
        document.querySelectorAll('.modalidad-card').forEach(card => card.classList.remove('selected'));

    } catch (err) {
        console.error(err);
        showError('Error al procesar la cita. Verifica que el servidor esté ejecutándose.');
    } finally {
        // Restaurar botón
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
        actualizarBotonSegunModalidad();
    }
});

// Función para formatear fecha
function formatearFecha(fecha) {
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(fecha).toLocaleDateString('es-ES', opciones);
}

// Función para obtener nombre del terapeuta
function obtenerNombreTerapeuta(idTerapeuta) {
    const selectTerapeuta = document.getElementById('idTerapeuta');
    const option = selectTerapeuta.querySelector(`option[value="${idTerapeuta}"]`);
    return option ? option.textContent : 'Terapeuta no especificado';
}



// Función para mostrar errores
function showError(message) {
    // Crear alerta de error si no existe
    let alertDiv = document.getElementById('error-alert');
    if (!alertDiv) {
        alertDiv = document.createElement('div');
        alertDiv.id = 'error-alert';
        alertDiv.className = 'alert alert-danger alert-dismissible fade show';
        alertDiv.innerHTML = `
            <i class="fas fa-exclamation-triangle me-2"></i>
            <span id="error-message"></span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        document.querySelector('.form-body').insertBefore(alertDiv, document.querySelector('.info-box'));
    }
    
    document.getElementById('error-message').textContent = message;
    alertDiv.style.display = 'block';
    
    // Auto-ocultar después de 5 segundos
    setTimeout(() => {
        alertDiv.style.display = 'none';
    }, 5000);
}

// Función para mostrar éxito
function showSuccess(message) {
    // Crear alerta de éxito si no existe
    let alertDiv = document.getElementById('success-alert');
    if (!alertDiv) {
        alertDiv = document.createElement('div');
        alertDiv.id = 'success-alert';
        alertDiv.className = 'alert alert-success alert-dismissible fade show';
        alertDiv.innerHTML = `
            <i class="fas fa-check-circle me-2"></i>
            <span id="success-message"></span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        document.querySelector('.form-body').insertBefore(alertDiv, document.querySelector('.info-box'));
    }
    
    document.getElementById('success-message').textContent = message;
    alertDiv.style.display = 'block';
    
    // Auto-ocultar después de 3 segundos
    setTimeout(() => {
        alertDiv.style.display = 'none';
    }, 3000);
}

// Validación de fecha mínima (hoy en adelante)
document.getElementById('fecha').addEventListener('change', function() {
    const today = new Date().toISOString().split('T')[0];
    
    if (this.value < today) {
        showError('No puedes seleccionar una fecha anterior a hoy. Las citas deben ser para hoy en adelante.');
        this.value = '';
        return;
    }
    
    // Regenerar slots de tiempo cuando cambia la fecha
    generarTimeSlots();
});

// Manejar tema personalizado
document.getElementById('tema').addEventListener('change', function() {
    const temaPersonalizado = document.getElementById('temaPersonalizado');
    const temaOtro = document.getElementById('temaOtro');
    
    if (this.value === 'Otro') {
        temaPersonalizado.style.display = 'block';
        temaOtro.required = true;
    } else {
        temaPersonalizado.style.display = 'none';
        temaOtro.required = false;
        temaOtro.value = '';
    }
});

// Función para crear confeti mejorado (menos exagerado)
function crearConfeti() {
    const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57', '#ff9ff3', '#54a0ff', '#5f27cd'];
    
    for (let i = 0; i < 80; i++) {
        setTimeout(() => {
            const confeti = document.createElement('div');
            confeti.className = 'confetti';
            confeti.style.left = Math.random() * 100 + 'vw';
            confeti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
            confeti.style.animationDelay = Math.random() * 2 + 's';
            confeti.style.animationDuration = (Math.random() * 2 + 3) + 's';
            confeti.style.zIndex = '9999';
            confeti.style.transform = `rotate(${Math.random() * 360}deg)`;
            
            document.body.appendChild(confeti);
            
            // Remover confeti después de la animación
            setTimeout(() => {
                if (confeti.parentNode) {
                    confeti.parentNode.removeChild(confeti);
                }
            }, 6000);
        }, i * 50);
    }
}

// Función para crear fuegos artificiales mejorados (menos exagerado)
function crearFuegosArtificiales() {
    const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57', '#ff9ff3', '#54a0ff', '#5f27cd'];
    
    // Crear overlay de celebración
    const overlay = document.createElement('div');
    overlay.className = 'celebration-overlay';
    overlay.style.zIndex = '9998';
    document.body.appendChild(overlay);
    
    // Crear fuegos artificiales en diferentes posiciones
    for (let i = 0; i < 8; i++) {
        setTimeout(() => {
            const x = Math.random() * window.innerWidth;
            const y = Math.random() * (window.innerHeight * 0.6);
            
            for (let j = 0; j < 25; j++) {
                const firework = document.createElement('div');
                firework.className = 'firework';
                firework.style.left = x + 'px';
                firework.style.top = y + 'px';
                firework.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
                firework.style.transform = `translate(${(Math.random() - 0.5) * 300}px, ${(Math.random() - 0.5) * 300}px)`;
                firework.style.zIndex = '9999';
                
                document.body.appendChild(firework);
                
                // Remover fuego artificial después de la animación
                setTimeout(() => {
                    if (firework.parentNode) {
                        firework.parentNode.removeChild(firework);
                    }
                }, 3000);
            }
        }, i * 200);
    }
    
    // Remover overlay después de la animación
    setTimeout(() => {
        if (overlay.parentNode) {
            overlay.parentNode.removeChild(overlay);
        }
    }, 3000);
}

// Función para crear brillos (menos exagerado)
function crearBrillos() {
    for (let i = 0; i < 30; i++) {
        setTimeout(() => {
            const sparkle = document.createElement('div');
            sparkle.className = 'sparkle';
            sparkle.style.left = Math.random() * 100 + 'vw';
            sparkle.style.top = Math.random() * 100 + 'vh';
            sparkle.style.zIndex = '9999';
            
            document.body.appendChild(sparkle);
            
            // Remover brillo después de la animación
            setTimeout(() => {
                if (sparkle.parentNode) {
                    sparkle.parentNode.removeChild(sparkle);
                }
            }, 2000);
        }, i * 150);
    }
}

// Función para crear globos (menos exagerado)
function crearGlobos() {
    const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57', '#ff9ff3', '#54a0ff', '#5f27cd'];
    
    for (let i = 0; i < 5; i++) {
        setTimeout(() => {
            const balloon = document.createElement('div');
            balloon.className = 'balloon';
            balloon.style.left = Math.random() * 100 + 'vw';
            balloon.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
            balloon.style.zIndex = '9999';
            
            document.body.appendChild(balloon);
            
            // Remover globo después de la animación
            setTimeout(() => {
                if (balloon.parentNode) {
                    balloon.parentNode.removeChild(balloon);
                }
            }, 6000);
        }, i * 400);
    }
}

// Función para celebrar éxito mejorada (menos exagerada)
function celebrarExito() {
    console.log('🎉 Iniciando celebración...');
    
    crearConfeti();
    crearFuegosArtificiales();
    crearBrillos();
    crearGlobos();
    
    console.log('✅ Celebración completada');
    
    // Reproducir sonido de éxito (opcional)
    // const audio = new Audio('path/to/success-sound.mp3');
    // audio.play();
}



// Cargar niños del padre logueado
async function cargarNinos() {
    try {
        // Obtener el ID del padre logueado (esto debe venir de la sesión)
        // Por ahora usaremos un ID de prueba, pero esto debe ser dinámico
        const idPadre = obtenerIdPadreLogueado(); // Esta función debe implementarse según tu sistema de autenticación
        
        if (!idPadre) {
            console.error('No se pudo obtener el ID del padre logueado');
            return;
        }
        
        const response = await fetch(`http://localhost:3000/ninos/${idPadre}`);
        const ninos = await response.json();
        
        const selectNino = document.getElementById('idNino');
        selectNino.innerHTML = '<option value="">Selecciona un niño</option>';
        
        if (ninos.length === 0) {
            selectNino.innerHTML = '<option value="">No tienes niños registrados</option>';
            return;
        }
        
        ninos.forEach(nino => {
            const option = document.createElement('option');
            option.value = nino.id_nino;
            
            // Calcular edad del niño
            const fechaNac = new Date(nino.fecha_nacimiento);
            const hoy = new Date();
            const edad = hoy.getFullYear() - fechaNac.getFullYear();
            const mes = hoy.getMonth() - fechaNac.getMonth();
            const edadFinal = mes < 0 || (mes === 0 && hoy.getDate() < fechaNac.getDate()) ? edad - 1 : edad;
            
            option.textContent = `${nino.nombre} (${edadFinal} años)`;
            selectNino.appendChild(option);
        });
        
        // Si solo hay un niño, seleccionarlo automáticamente
        if (ninos.length === 1) {
            selectNino.value = ninos[0].id_nino;
        }
        
    } catch (error) {
        console.error('Error al cargar niños:', error);
        const selectNino = document.getElementById('idNino');
        selectNino.innerHTML = '<option value="">Error al cargar niños</option>';
    }
}

// Función para obtener el ID del padre logueado
function obtenerIdPadreLogueado() {
    // Opción 1: Si tienes el ID en la URL como parámetro (principal)
    const urlParams = new URLSearchParams(window.location.search);
    const idPadreUrl = urlParams.get('idPadre');
    if (idPadreUrl) {
        console.log('ID del padre obtenido de URL:', idPadreUrl);
        return idPadreUrl;
    }
    
    // Opción 2: Si tienes el ID en localStorage
    const idPadre = localStorage.getItem('idPadre');
    if (idPadre) {
        console.log('ID del padre obtenido de localStorage:', idPadre);
        return idPadre;
    }
    
    // Opción 3: Si tienes el ID en una variable global
    if (typeof window.idPadreLogueado !== 'undefined') {
        console.log('ID del padre obtenido de variable global:', window.idPadreLogueado);
        return window.idPadreLogueado;
    }
    
    // Opción 4: IDs de prueba (temporal)
    // Para el padre "p" (ID: 2) y "Laura Gómez" (ID: 1)
    const usuarioActual = localStorage.getItem('usuarioActual') || 'p';
    if (usuarioActual === 'p') return '2'; // Padre "p"
    if (usuarioActual === 'laura') return '1'; // Padre "Laura Gómez"
    
    // Por defecto, usar el padre "p"
    console.log('Usando ID de padre por defecto: 2');
    return '2';
}

// Cargar terapeutas desde la base de datos
async function cargarTerapeutas() {
    try {
        const response = await fetch('http://localhost:3000/terapeutas');
        const terapeutas = await response.json();
        
        const selectTerapeuta = document.getElementById('idTerapeuta');
        selectTerapeuta.innerHTML = '<option value="">Selecciona un terapeuta</option>';
        
        terapeutas.forEach(terapeuta => {
            const option = document.createElement('option');
            option.value = terapeuta.id_usuario;
            option.textContent = `${terapeuta.nombre} - ${terapeuta.especialidad}`;
            selectTerapeuta.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar terapeutas:', error);
        // Mantener opciones por defecto si hay error
    }
}

// Función para guardar cita en la base de datos
async function guardarCitaEnBD(datosCita) {
    try {
        const response = await fetch('http://localhost:3000/guardar-cita', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datosCita)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            console.log('Cita guardada exitosamente:', result);
            return true;
        } else {
            console.error('Error al guardar cita:', result.error);
            return false;
        }
    } catch (error) {
        console.error('Error al guardar cita:', error);
        return false;
    }
}

// Inicializar
document.addEventListener('DOMContentLoaded', function() {
    // Establecer fecha mínima como hoy
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('fecha').min = today;
    
    generarTimeSlots();
    cargarTerapeutas();
    cargarNinos(); // Cargar niños del padre logueado
});
