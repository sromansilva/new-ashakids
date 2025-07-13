
document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.querySelector(".menu-toggle");
  const sidebar = document.querySelector(".sidebar");

  const handleResize = () => {
    const isMobile = window.innerWidth <= 768;

    if (!isMobile) {
      sidebar.classList.add("active");
      sidebar.classList.remove("hidden");
    } else {
      sidebar.classList.remove("active");
      sidebar.classList.add("hidden");
    }
  };

  toggleBtn.addEventListener("click", () => {
    sidebar.classList.toggle("active");
    sidebar.classList.toggle("hidden");
  });

  window.addEventListener("resize", handleResize);
  handleResize(); // Ejecutar al cargar
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