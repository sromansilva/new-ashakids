function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('show');
    document.querySelector('.sidebar-backdrop').classList.toggle('show');
}

function editarUsuario(id) {
    const usuario = usuariosCargados.find(u => u.id_usuario === id);
    if (!usuario) return;
    document.getElementById('editIdUsuario').value = usuario.id_usuario;
    document.getElementById('editNombre').value = usuario.nombre || '';
    document.getElementById('editCorreo').value = usuario.correo || '';
    document.getElementById('editCodigo').value = usuario.codigo || '';
    document.getElementById('editRol').value = usuario.rol || '';
    document.getElementById('editTelefono').value = usuario.telefono || '';
}

function cambiarPassword(id) {
    document.getElementById('passwordIdUsuario').value = id;
}

function eliminarUsuario(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este usuario?')) {
        fetch('/admin/usuarios/eliminar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'id_usuario=' + encodeURIComponent(id)
        })
        .then(resp => {
            if (resp.ok) {
                cargarUsuarios();
                alert('Usuario eliminado correctamente');
            } else {
                return resp.text().then(error => { throw new Error(error); });
            }
        })
        .catch(err => {
            alert('Error: ' + err.message);
        });
    }
}

// Referencias a los elementos de filtro y tabla
const buscarInput = document.getElementById('buscarUsuario');
const filtroRol = document.getElementById('filtroRol');
const filtroEstado = document.getElementById('filtroEstado');
const tablaUsuariosBody = document.querySelector('table tbody');

// Variable global para los usuarios cargados
let usuariosCargados = [];

// Función para cargar y renderizar la tabla de usuarios
async function cargarUsuarios() {
    const busqueda = buscarInput.value;
    const rol = filtroRol.value;
    const estado = filtroEstado.value;
    let url = `/admin/usuarios/lista?`;
    if (busqueda) url += `busqueda=${encodeURIComponent(busqueda)}&`;
    if (rol) url += `rol=${encodeURIComponent(rol)}&`;
    if (estado) url += `estado=${encodeURIComponent(estado)}&`;

    const resp = await fetch(url);
    const usuarios = await resp.json();
    usuariosCargados = usuarios; // Guardar para acceso rápido
    renderizarTabla(usuarios);
}

// Función para renderizar la tabla
function renderizarTabla(usuarios) {
    tablaUsuariosBody.innerHTML = '';
    if (!usuarios.length) {
        tablaUsuariosBody.innerHTML = '<tr><td colspan="8" class="text-center">No se encontraron usuarios</td></tr>';
        return;
    }
    usuarios.forEach(usuario => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${usuario.id_usuario}</td>
            <td>${usuario.nombre}</td>
            <td>${usuario.correo}</td>
            <td>${usuario.codigo}</td>
            <td><span class="badge ${usuario.rol === 'padre' ? 'bg-primary' : (usuario.rol === 'terapeuta' ? 'bg-success' : 'bg-warning')}">${usuario.rol}</span></td>
            <td>${usuario.telefono || ''}</td>
            <td><span class="badge ${usuario.disponible === false ? 'bg-secondary' : 'bg-success'}">${usuario.disponible === false ? 'Inactivo' : 'Activo'}</span></td>
            <td>
                <div class="btn-group" role="group">
                    <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editarUsuarioModal" onclick="editarUsuario(${usuario.id_usuario})"><i class="fas fa-edit"></i></button>
                    <button class="btn btn-sm btn-outline-warning" data-bs-toggle="modal" data-bs-target="#cambiarPasswordModal" onclick="cambiarPassword(${usuario.id_usuario})"><i class="fas fa-key"></i></button>
                    <button class="btn btn-sm btn-outline-danger" onclick="eliminarUsuario(${usuario.id_usuario})"><i class="fas fa-trash"></i></button>
                </div>
            </td>
        `;
        tablaUsuariosBody.appendChild(tr);
    });
}

// Eventos de filtros y búsqueda
buscarInput.addEventListener('input', cargarUsuarios);
filtroRol.addEventListener('change', cargarUsuarios);
filtroEstado.addEventListener('change', cargarUsuarios);

// Cargar usuarios al inicio
window.addEventListener('DOMContentLoaded', cargarUsuarios);

// Agregar usuario (AJAX)
document.querySelector('#agregarUsuarioModal form').addEventListener('submit', async function (e) {
    e.preventDefault();
    const form = e.target;
    const data = {
        nombre: form.nombre.value,
        correo: form.correo.value,
        codigo: form.codigo.value,
        contrasena: form.contrasena.value,
        rol: form.rol.value,
        telefono: form.telefono.value
    };
    try {
        const resp = await fetch('/admin/usuarios/agregar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (resp.ok) {
            // Cerrar modal y limpiar formulario
            const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('agregarUsuarioModal'));
            modal.hide();
            form.reset();
            cargarUsuarios();
            alert('Usuario agregado correctamente');
        } else {
            const error = await resp.text();
            alert('Error: ' + error);
        }
    } catch (err) {
        alert('Error de red o servidor');
    }
});

// Editar usuario (AJAX)
document.querySelector('#editarUsuarioModal form').addEventListener('submit', async function (e) {
    e.preventDefault();
    const form = e.target;
    const data = {
        id_usuario: form.id_usuario.value,
        nombre: form.nombre.value,
        correo: form.correo.value,
        codigo: form.codigo.value,
        rol: form.rol.value,
        telefono: form.telefono.value
    };
    try {
        const resp = await fetch('/admin/usuarios/editar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (resp.ok) {
            // Cerrar modal y limpiar formulario
            const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('editarUsuarioModal'));
            modal.hide();
            form.reset();
            cargarUsuarios();
            alert('Usuario editado correctamente');
        } else {
            const error = await resp.text();
            alert('Error: ' + error);
        }
    } catch (err) {
        alert('Error de red o servidor');
    }
});

// Cambiar contraseña (AJAX)
document.querySelector('#cambiarPasswordModal form').addEventListener('submit', async function (e) {
    e.preventDefault();
    const form = e.target;
    const id_usuario = form.id_usuario.value;
    const nuevaContrasena = form.nuevaContrasena.value;
    const confirmarContrasena = form.confirmarContrasena.value;
    if (nuevaContrasena !== confirmarContrasena) {
        alert('Las contraseñas no coinciden');
        return;
    }
    try {
        const params = new URLSearchParams();
        params.append('id_usuario', id_usuario);
        params.append('nuevaContrasena', nuevaContrasena);
        const resp = await fetch('/admin/usuarios/cambiar-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString()
        });
        if (resp.ok) {
            // Cerrar modal y limpiar formulario
            const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('cambiarPasswordModal'));
            modal.hide();
            form.reset();
            cargarUsuarios();
            alert('Contraseña cambiada correctamente');
        } else {
            const error = await resp.text();
            alert('Error: ' + error);
        }
    } catch (err) {
        alert('Error de red o servidor');
    }
});