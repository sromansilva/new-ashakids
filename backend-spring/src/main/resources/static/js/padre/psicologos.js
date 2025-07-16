// Cargar psicólogos y renderizar tarjetas
async function cargarPsicologos() {
  try {
    // Suponiendo que hay un endpoint que devuelve todos los terapeutas
    const resp = await fetch('/api/usuarios/terapeutas');
    const psicologos = await resp.json();
    const contenedor = document.getElementById('psicologos-lista');
    contenedor.innerHTML = '';
    psicologos.forEach(psicologo => {
      const card = document.createElement('div');
      card.className = 'col-md-4';
      card.innerHTML = `
        <div class="card shadow-sm h-100">
          <img src="${psicologo.fotoUrl || 'https://cdn-icons-png.flaticon.com/512/1077/1077114.png'}" class="card-img-top" alt="${psicologo.nombre}" style="object-fit:cover;height:250px;">
          <div class="card-body">
            <h5 class="card-title">${psicologo.nombre}</h5>
            <p class="card-text"><strong>Especialidad:</strong> ${psicologo.especialidad || ''}</p>
            <p class="card-text">${psicologo.descripcionCorta || ''}</p>
            <button class="btn btn-outline-primary w-100 btn-ver-perfil" data-id="${psicologo.id_usuario}">Ver perfil</button>
          </div>
        </div>
      `;
      contenedor.appendChild(card);
    });
    // Asignar eventos a los botones de ver perfil
    document.querySelectorAll('.btn-ver-perfil').forEach(btn => {
      btn.addEventListener('click', function() {
        mostrarModalPerfilPsicologo(this.dataset.id);
      });
    });
  } catch (err) {
    console.error('Error cargando psicólogos:', err);
  }
}

// Cargar paquetes y permitir seleccionar uno
async function cargarPaquetesParaPsicologo(idTerapeuta) {
  try {
    const resp = await fetch('/api/paquetes');
    const paquetes = await resp.json();
    const lista = document.getElementById('paquetes-lista');
    lista.innerHTML = '';
    let paqueteSeleccionado = null;
    paquetes.forEach(paquete => {
      const btn = document.createElement('button');
      btn.className = 'btn btn-outline-secondary paquete-btn';
      btn.textContent = `${paquete.nombre} (${paquete.horas}h) - S/.${paquete.precio}`;
      btn.onclick = () => {
        document.querySelectorAll('.paquete-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        paqueteSeleccionado = paquete;
        document.getElementById('paquete-seleccionado-info').textContent = `Seleccionado: ${paquete.nombre} (${paquete.horas}h) - S/.${paquete.precio}`;
        document.getElementById('btn-comprar-paquete').disabled = false;
        document.getElementById('btn-comprar-paquete').dataset.paqueteId = paquete.id_paquete;
        document.getElementById('btn-comprar-paquete').dataset.terapeutaId = idTerapeuta;
      };
      lista.appendChild(btn);
    });
    // Deshabilitar botón de compra hasta seleccionar
    document.getElementById('btn-comprar-paquete').disabled = true;
    document.getElementById('paquete-seleccionado-info').textContent = '';
  } catch (err) {
    document.getElementById('paquetes-lista').innerHTML = '<div class="text-danger">No se pudieron cargar los paquetes</div>';
  }
}

// Comprar paquete
async function comprarPaquete() {
  const btn = document.getElementById('btn-comprar-paquete');
  const idPaquete = btn.dataset.paqueteId;
  const idTerapeuta = btn.dataset.terapeutaId;
  if (!idPaquete || !idTerapeuta) return;
  btn.disabled = true;
  btn.textContent = 'Procesando...';
  try {
    const resp = await fetch('/api/paquetes/comprar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: `idPaquete=${idPaquete}&idTerapeuta=${idTerapeuta}`
    });
    if (resp.ok) {
      document.getElementById('compra-estado').innerHTML = '<span class="text-success">¡Compra realizada! Esperando confirmación de pago.</span>';
      btn.textContent = 'Comprar paquete';
      btn.disabled = true;
      await actualizarEstadoPaqueteYHoras(idTerapeuta);
    } else {
      document.getElementById('compra-estado').innerHTML = '<span class="text-danger">Error al comprar paquete</span>';
      btn.textContent = 'Comprar paquete';
      btn.disabled = false;
    }
  } catch (err) {
    document.getElementById('compra-estado').innerHTML = '<span class="text-danger">Error al comprar paquete</span>';
    btn.textContent = 'Comprar paquete';
    btn.disabled = false;
  }
}

async function actualizarEstadoPaqueteYHoras(idTerapeuta) {
  const padreId = obtenerIdPadreLogueado();
  const estadoDiv = document.getElementById('compra-estado');
  const btnComprar = document.getElementById('btn-comprar-paquete');
  const agendarDiv = document.getElementById('agendar-sesion-info');
  const horasDiv = document.getElementById('horas-disponibles-info');
  const btnAgendar = document.getElementById('btn-agendar-sesion');

  try {
    // 1. Consultar compras activas
    const respCompras = await fetch('/api/paquetes/compras-activas');
    const compras = await respCompras.json();
    const compraConEste = compras.find(c => c.terapeuta.id_usuario == idTerapeuta);

    // 2. Consultar compras pendientes
    const respPend = await fetch('/api/paquetes/compras-pendientes-padre');
    const pendientes = await respPend.json();
    const pendienteConEste = pendientes.find(c => c.terapeuta.id_usuario == idTerapeuta);

    // 3. Consultar horas disponibles (si hay compra activa)
    let horas = 0;
    if (compraConEste) {
      const respHoras = await fetch(`/api/paquetes/horas-disponibles?padreId=${padreId}&terapeutaId=${idTerapeuta}`);
      const dataHoras = await respHoras.json();
      horas = dataHoras.horasDisponibles || 0;
    }

    // 4. Actualizar UI según estado
    if (compraConEste) {
      estadoDiv.innerHTML = '<span class="text-success">Tienes un paquete activo con este terapeuta.</span>';
      btnComprar.disabled = true;
      horasDiv.classList.remove('d-none');
      horasDiv.textContent = `Horas disponibles: ${horas}`;
      if (horas > 0) {
        agendarDiv.classList.remove('d-none');
        btnAgendar.disabled = false;
      } else {
        agendarDiv.classList.add('d-none');
        btnAgendar.disabled = true;
      }
    } else if (pendienteConEste) {
      estadoDiv.innerHTML = '<span class="text-warning">Tienes una compra pendiente de confirmación con este terapeuta.</span>';
      btnComprar.disabled = true;
      horasDiv.classList.add('d-none');
      agendarDiv.classList.add('d-none');
      btnAgendar.disabled = true;
    } else {
      estadoDiv.innerHTML = '';
      btnComprar.disabled = false;
      horasDiv.classList.add('d-none');
      agendarDiv.classList.add('d-none');
      btnAgendar.disabled = true;
    }
  } catch (err) {
    estadoDiv.innerHTML = '<span class="text-danger">No se pudo consultar el estado de compra</span>';
    btnComprar.disabled = false;
    horasDiv.classList.add('d-none');
    agendarDiv.classList.add('d-none');
    btnAgendar.disabled = true;
  }
}

// Modificar mostrarModalPerfilPsicologo para actualizar estado
async function mostrarModalPerfilPsicologo(idPsicologo) {
  try {
    // Suponiendo que hay un endpoint para obtener detalles del terapeuta
    const resp = await fetch(`/api/usuarios/terapeuta/${idPsicologo}`);
    const psicologo = await resp.json();
    document.getElementById('psicologo-foto').src = psicologo.fotoUrl || 'https://cdn-icons-png.flaticon.com/512/1077/1077114.png';
    document.getElementById('psicologo-nombre').textContent = psicologo.nombre;
    document.getElementById('psicologo-especialidad').textContent = psicologo.especialidad || '';
    document.getElementById('psicologo-correo').textContent = psicologo.correo || '';
    document.getElementById('psicologo-descripcion').textContent = psicologo.descripcionLarga || '';
    // Cargar paquetes
    cargarPaquetesParaPsicologo(idPsicologo);
    // Actualizar estado de compra y horas
    await actualizarEstadoPaqueteYHoras(idPsicologo);
    // Mostrar el modal
    const modal = new bootstrap.Modal(document.getElementById('modalPerfilPsicologo'));
    modal.show();
  } catch (err) {
    console.error('Error cargando perfil de psicólogo:', err);
  }
}

// Función para obtener el ID del padre logueado (ajusta según tu sistema)
function obtenerIdPadreLogueado() {
  // Ejemplo: si lo tienes en localStorage o variable global
  return localStorage.getItem('idPadre') || window.idPadreLogueado || '2'; // Ajusta el valor por defecto si es necesario
}

// Función para consultar horas disponibles con un terapeuta
async function consultarHorasDisponibles(padreId, terapeutaId) {
  try {
      const response = await fetch(`/api/paquetes/horas-disponibles?padreId=${padreId}&terapeutaId=${terapeutaId}`);
      if (!response.ok) throw new Error('Error consultando horas');
      const data = await response.json();
      return data.horasDisponibles || 0;
  } catch (e) {
      console.error(e);
      return 0;
  }
}

// Llama a esta función cuando abras el modal de un terapeuta
async function actualizarHorasYBotonAgendar(terapeutaId) {
  const padreId = obtenerIdPadreLogueado();
  const horas = await consultarHorasDisponibles(padreId, terapeutaId);

  const horasInfo = document.getElementById('horas-disponibles-info');
  const agendarDiv = document.getElementById('agendar-sesion-info');
  const btnAgendar = document.getElementById('btn-agendar-sesion');

  if (horas > 0) {
      horasInfo.textContent = `Tienes ${horas} hora${horas > 1 ? 's' : ''} disponible${horas > 1 ? 's' : ''} con este especialista.`;
      horasInfo.classList.remove('d-none', 'alert-danger');
      horasInfo.classList.add('alert-info');
      agendarDiv.classList.remove('d-none');
      btnAgendar.disabled = false;
  } else {
      horasInfo.textContent = 'No tienes horas disponibles con este especialista. Compra un paquete para agendar.';
      horasInfo.classList.remove('d-none', 'alert-info');
      horasInfo.classList.add('alert-danger');
      agendarDiv.classList.add('d-none');
      btnAgendar.disabled = true;
  }
}

document.addEventListener('DOMContentLoaded', cargarPsicologos);
document.getElementById('btn-comprar-paquete').onclick = comprarPaquete; 