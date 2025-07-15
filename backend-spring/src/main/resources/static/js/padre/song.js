
function showSong(songId) {
    // Ocultar todas las secciones de canciones
    const songs = document.querySelectorAll('.video-lyrics');
    songs.forEach(song => song.style.display = 'none');

    // Mostrar la sección de la canción seleccionada
    document.getElementById(songId).style.display = 'block';

    // Ocultar el contenedor anaranjado al mostrar una canción
    hideOrangeContainer();
}

function hideOrangeContainer() {
    const orangeContainer = document.getElementById('orangeContainer');
    if (orangeContainer) {
        orangeContainer.style.display = 'none';
    }
}

// Función para asegurar que el contenedor anaranjado se muestre al inicio
function showOrangeContainer() {
    const orangeContainer = document.getElementById('orangeContainer');
    if (orangeContainer) {
        orangeContainer.style.display = 'flex';
    }
}

// Al cargar la página, mostrar el contenedor anaranjado
window.addEventListener('load', showOrangeContainer);

