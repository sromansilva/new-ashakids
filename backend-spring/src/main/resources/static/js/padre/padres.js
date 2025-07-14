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
*/