const menuBtn = document.querySelector('.menu-btn');
const navbar = document.querySelector('.navbar');
let menuOpen = false;

document.getElementById('jugar-btn').addEventListener('click', mostrarOpciones);


menuBtn.addEventListener('click', () => {
    if(!menuOpen) {
        menuBtn.classList.add('open');
        navbar.classList.add('active');
        menuOpen = true;
    } else {
        menuBtn.classList.remove('open');
        navbar.classList.remove('active');
        menuOpen = false;
    }
});

// Cerrar menú al hacer clic en un enlace
const navLinks = document.querySelectorAll('.nav-list a');
navLinks.forEach(link => {
    link.addEventListener('click', () => {
        menuBtn.classList.remove('open');
        navbar.classList.remove('active');
        menuOpen = false;
    });
});

const adivinanzas = [
    {
        pregunta: "Tengo alas y pico. Hablo y hablo, pero no sé lo que digo. ¿Quién soy?",
        opciones: [
            { texto: "Perico", imagen: "Adivinanza/Perico.svg" },
            { texto: "Pato", imagen: "Adivinanza/Pato.svg" },
            { texto: "Águila", imagen: "Adivinanza/Aguila.svg" }
        ],
        respuestaCorrecta: "Perico"
    },
    {
        pregunta: "Grande, muy grande, mayor que la Tierra. Arde y no se quema, quema y no es candela.",
        opciones: [
            { texto: "El Sol", imagen: "Adivinanza/Sol.svg" },
            { texto: "Volcán", imagen: "Adivinanza/Volcan.svg" },
            { texto: "Relámpago", imagen: "Adivinanza/Relampago.svg" }
        ],
        respuestaCorrecta: "El Sol"
    },
    {
        pregunta: "Tengo dientes y no puedo masticar. ¿Quién soy?",
        opciones: [
            { texto: "Pez", imagen: "Adivinanza/Pez.svg" },
            { texto: "Tenedor", imagen: "Adivinanza/Tenedor.svg" },
            { texto: "León", imagen: "Adivinanza/Leon.svg" }
        ],
        respuestaCorrecta: "Tenedor"
    },
    {
        pregunta: "Llena de agujeros pero capaz de contener agua. ¿Qué soy?",
        opciones: [
            { texto: "Esponja", imagen: "Adivinanza/Esponja.svg" },
            { texto: "Colador", imagen: "Adivinanza/Colador.svg" },
            { texto: "Toalla", imagen: "Adivinanza/Toalla.svg" }
        ],
        respuestaCorrecta: "Esponja"
    },
    {
        pregunta: "Tengo orejas largas y dientes grandes, y zanahorias me encanta comer. ¿Quién soy?",
        opciones: [
            { texto: "Conejo", imagen: "Adivinanza/Conejo.svg" },
            { texto: "Ratón", imagen: "Adivinanza/Raton.svg" },
            { texto: "Ardilla", imagen: "Adivinanza/Ardilla.svg" }
        ],
        respuestaCorrecta: "Conejo"
    },
    {
        pregunta: "Soy verde por fuera, roja por dentro y tengo muchas semillas. ¿Qué soy?",
        opciones: [
            { texto: "Sandía", imagen: "Adivinanza/Sandia.svg" },
            { texto: "Manzana", imagen: "Adivinanza/Manzana.svg" },
            { texto: "Fresa", imagen: "Adivinanza/Fresa.svg" }
        ],
        respuestaCorrecta: "Sandía"
    },
    {
        pregunta: "Aunque soy pequeño y lento, llevo mi casa conmigo a donde voy. ¿Quién soy?",
        opciones: [
            { texto: "Tortuga", imagen: "Adivinanza/Tortuga.svg" },
            { texto: "Caracol", imagen: "Adivinanza/Caracol.svg" },
            { texto: "Cangrejo", imagen: "Adivinanza/Cangrejo.svg" }
        ],
        respuestaCorrecta: "Caracol"
    },
    {
        pregunta: "Tengo cuatro patas, pero no puedo caminar. ¿Quién soy?",
        opciones: [
            { texto: "Mesa", imagen: "Adivinanza/Mesa.svg" },
            { texto: "Gato", imagen: "Adivinanza/Gato.svg" },
            { texto: "Perro", imagen: "Adivinanza/Perro.svg" }
        ],
        respuestaCorrecta: "Mesa"
    },
    {
        pregunta: "Vivo en el agua y siempre estoy mojado. ¿Quién soy?",
        opciones: [
            { texto: "Pez", imagen: "Adivinanza/Pez.svg" },
            { texto: "Rana", imagen: "Adivinanza/Ranaa.svg" },
            { texto: "Gaviota", imagen: "Adivinanza/Gaviota.svg" }
        ],
        respuestaCorrecta: "Pez"
    },
    {
        pregunta: "Vuelo sin alas, lloro sin ojos. ¿Quién soy?",
        opciones: [
            { texto: "Nube", imagen: "Adivinanza/Nube.svg" },
            { texto: "Viento", imagen: "Adivinanza/Viento.svg" },
            { texto: "Lluvia", imagen: "Adivinanza/Lluvia.svg" }
        ],
        respuestaCorrecta: "Nube"
    }
];


let preguntaActual = 0;
let puntos = 0;

function mostrarOpciones() {
    const adivinanza = adivinanzas[preguntaActual];
    document.getElementById('adivinanzaTexto').textContent = adivinanza.pregunta;

    const imagenPregunta = document.getElementById('imagenPregunta');
    if (adivinanza.imagenPregunta) {
        imagenPregunta.src = adivinanza.imagenPregunta;
        imagenPregunta.style.display = 'block';
    } else {
        imagenPregunta.style.display = 'none';
    }

    const opcionesDiv = document.getElementById('opciones');
    opcionesDiv.innerHTML = "";

    adivinanza.opciones.forEach(opcion => {
        const opcionDiv = document.createElement('div');
        opcionDiv.classList.add('opcion');

        const imagen = document.createElement('img');
        imagen.src = opcion.imagen;
        imagen.alt = opcion.texto;
        imagen.classList.add('imagen-opcion');

        const texto = document.createElement('span');
        texto.textContent = opcion.texto;

        opcionDiv.appendChild(imagen);
        opcionDiv.appendChild(texto);
        opcionDiv.onclick = () => checkAnswer(opcion.texto);
        opcionesDiv.appendChild(opcionDiv);
    });

    document.getElementById('resultado').textContent = '';
    document.getElementById('modal').style.display = 'flex';
}

function checkAnswer(respuesta) {
    const adivinanza = adivinanzas[preguntaActual];
    const resultadoDiv = document.getElementById('resultado');
    const sonidoCorrecto = document.getElementById('sonidoCorrecto');
    const sonidoIncorrecto = document.getElementById('sonidoIncorrecto');

    if (respuesta === adivinanza.respuestaCorrecta) {
        resultadoDiv.textContent = "¡Correcto!";
        resultadoDiv.style.color = "green";
        sonidoCorrecto.play();
        puntos++; // Aumentar puntos si es correcto
    } else {
        resultadoDiv.textContent = "Incorrecto.";
        resultadoDiv.style.color = "red";
        sonidoIncorrecto.play();
    }

    // Esperar un poco antes de pasar a la siguiente adivinanza
    setTimeout(() => {
        siguienteAdivinanza();
    }, 1500);
}

function siguienteAdivinanza() {
    preguntaActual++;
    if (preguntaActual < adivinanzas.length) {
        mostrarOpciones();
    } else {
        mostrarResultadoFinal();
    }
}

function mostrarResultadoFinal() {
    const modalContenido = document.querySelector('.modal-content');

    // Limpiar contenido anterior
    modalContenido.innerHTML = '';

    // Crear el mensaje final
    const mensajeFinal = document.createElement('h3');
    const totalAdivinanzas = adivinanzas.length;
    mensajeFinal.textContent = `¡Terminaste! Obtuviste ${puntos} de ${totalAdivinanzas} puntos.`;

    if (puntos === totalAdivinanzas) {
        mensajeFinal.textContent += " ¡Excelente trabajo!";
    } else if (puntos >= totalAdivinanzas / 2) {
        mensajeFinal.textContent += " ¡Muy bien! Puedes intentarlo nuevamente para mejorar.";
    } else {
        mensajeFinal.textContent += " No te preocupes, puedes intentarlo de nuevo para mejorar tu puntuación.";
    }

    // Agregar mensaje y botón al modal
    modalContenido.appendChild(mensajeFinal);

    // Botón para reiniciar el juego
    const botonReiniciar = document.createElement('button');
    botonReiniciar.textContent = "Reintentar";
    botonReiniciar.classList.add('boton-reiniciar');
    botonReiniciar.onclick = reiniciarJuego;
    modalContenido.appendChild(botonReiniciar);

    document.getElementById('modal').style.display = 'flex';
}

function reiniciarJuego() {
    puntos = 0;
    preguntaActual = 0;
    cerrarModal(); // Cierra el modal antes de mostrar las opciones de nuevo
    setTimeout(() => {
        mostrarOpciones();
    }, 500); // Añadimos un pequeño delay para dar tiempo a que el modal se cierre antes de reiniciar
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

