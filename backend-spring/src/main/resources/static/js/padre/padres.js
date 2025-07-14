
document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.createElement("div");
  toggleBtn.className = "menu-toggle";
  toggleBtn.innerHTML = "☰";
  document.body.appendChild(toggleBtn);

  const sidebar = document.querySelector(".sidebar");

  toggleBtn.addEventListener("click", () => {
    sidebar.classList.toggle("active");
  });
});


function mostrarModalZoom(link) {
  const linkBtn = document.getElementById("linkReunion");
  linkBtn.href = link;

  const bsModal = new bootstrap.Modal(document.getElementById("modalReunion"));
  bsModal.show();
}


document.addEventListener("DOMContentLoaded", () => {
  vincularBotonesReservar();
});

document.addEventListener('DOMContentLoaded', function() {
  // Manejar clics en botones de cancelar cita
  document.addEventListener('click', function(e) {
    if (e.target.closest('.cancelar-cita-btn')) {
      const button = e.target.closest('.cancelar-cita-btn');
      const citaId = button.getAttribute('data-cita-id');
      
      if (confirm('¿Estás seguro de que quieres cancelar esta cita? Esta acción no se puede deshacer.')) {
        cancelarCita(citaId, button);
      }
    }
  });
  
  async function cancelarCita(citaId, button) {
    try {
      const response = await fetch(`http://localhost:3000/cancelar-cita/${citaId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      const result = await response.json();
      
      if (response.ok) {
        // Mostrar mensaje de éxito
        alert('Cita cancelada exitosamente');
        
        // Remover la tarjeta de la cita del DOM
        const card = button.closest('.col-12, .col-md-6, .col-lg-4, .cita-card');
        if (card) {
          card.remove();
        }
        
        // Si no quedan citas, recargar la página
        const remainingCards = document.querySelectorAll('.cancelar-cita-btn');
        if (remainingCards.length === 0) {
          location.reload();
        }
      } else {
        alert('Error al cancelar la cita: ' + (result.error || 'Error desconocido'));
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error al cancelar la cita. Verifica que el servidor esté ejecutándose.');
    }
  }
});