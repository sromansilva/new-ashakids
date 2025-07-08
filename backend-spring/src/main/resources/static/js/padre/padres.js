
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