document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.querySelector(".menu-toggle");
  const sidebar = document.querySelector(".sidebar");
  const overlay = document.getElementById("sidebar-overlay");

  const handleResize = () => {
    const isMobile = window.innerWidth <= 768;

    if (!sidebar) return;

    if (!isMobile) {
      sidebar.classList.add("active");
      sidebar.classList.remove("hidden");
      if (overlay) overlay.classList.add("hidden");
    } else {
      sidebar.classList.remove("active");
      sidebar.classList.add("hidden");
      if (overlay) overlay.classList.add("hidden");
    }
  };

  if (toggleBtn) {
    toggleBtn.addEventListener("click", () => {
      sidebar.classList.toggle("active");
      sidebar.classList.toggle("hidden");

      if (overlay) {
        if (sidebar.classList.contains("active")) {
          overlay.classList.remove("hidden");
        } else {
          overlay.classList.add("hidden");
        }
      }
    });
  }

  if (overlay) {
    overlay.addEventListener("click", () => {
      sidebar.classList.remove("active");
      sidebar.classList.add("hidden");
      overlay.classList.add("hidden");
    });
  }

  window.addEventListener("resize", handleResize);
  handleResize();
});

/*
function mostrarModalZoom(link) {
  const linkBtn = document.getElementById("linkReunion");
  linkBtn.href = link;

  const bsModal = new bootstrap.Modal(document.getElementById("modalReunion"));
  bsModal.show();
}


document.addEventListener("DOMContentLoaded", () => {
  vincularBotonesReservar();
});
<<<<<<< HEAD

=======
*/
>>>>>>> e21576c8f62f4e8817ccc426d4de95b2bbc7687c
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
<<<<<<< HEAD
});
=======
});
>>>>>>> e21576c8f62f4e8817ccc426d4de95b2bbc7687c
