// Datos de los cuentos
const cuentos = {
    cuento1: {
        titulo: "Aventura con Alea",
        contenido: `
        <div style="position: relative; width: 100%; height: 0; padding-top: 56.25%; 
                    box-shadow: 0 2px 8px rgba(63, 69, 81, 0.16); border-radius: 8px; overflow: hidden;">
            <iframe loading="lazy" style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; border: none;"
                src="https://www.canva.com/design/DAGVF4VGIUI/zPuROZUTEcqIqe9Gi-lZiA/view?embed" allowfullscreen>
            </iframe>
        </div>
        <p style="text-align: center; margin-top: 1em;">
            <a href="https://www.canva.com/design/DAGVF4VGIUI/zPuROZUTEcqIqe9Gi-lZiA/view" 
               target="_blank" rel="noopener" style="text-decoration: none; color: #2a7ae4; font-weight: bold;">
                Ver la presentación "Stella the Spacewoman" en Canva
            </a>
        </p>`
    },
    cuento2: {
        titulo: "La Gran Misión del Bosque Sorpresa",
        contenido: `
        <div style="position: relative; width: 100%; height: 0; padding-top: 56.25%; 
                    box-shadow: 0 2px 8px rgba(63, 69, 81, 0.16); border-radius: 8px; overflow: hidden;">
            <iframe loading="lazy" style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; border: none;"
                src="https://www.canva.com/design/DAGU00S6a94/42VXkPHYiWaoGrtH8tiU7g/view?embed" allowfullscreen>
            </iframe>
        </div>
        <p style="text-align: center; margin-top: 1em;">
            <a href="https://www.canva.com/design/DAGU00S6a94/42VXkPHYiWaoGrtH8tiU7g/view" 
               target="_blank" rel="noopener" style="text-decoration: none; color: #2a7ae4; font-weight: bold;">
                Ver la presentación "Cuento 2" en Canva
            </a>
        </p>`
    },
    cuento3: {
        titulo: "La búsquedad de la Cueva de Cristal",
        contenido: `
        <div style="position: relative; width: 100%; height: 0; padding-top: 56.25%; 
                    box-shadow: 0 2px 8px rgba(63, 69, 81, 0.16); border-radius: 8px; overflow: hidden;">
            <iframe loading="lazy" style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; border: none;"
                src="https://www.canva.com/design/DAGW3BvWKd4/AzUUR58xqPSUpcUH0TjjtQ/view?embed" allowfullscreen>
            </iframe>
        </div>
        <p style="text-align: center; margin-top: 1em;">
            <a href="https://www.canva.com/design/DAGW3BvWKd4/AzUUR58xqPSUpcUH0TjjtQ/view" 
               target="_blank" rel="noopener" style="text-decoration: none; color: #2a7ae4; font-weight: bold;">
                Ver la presentación "Cuento 3" en Canva
            </a>
        </p>`
    },
    cuento4: {
        titulo: "Dondé están mis botas?",
        contenido: `
        <div style="position: relative; width: 100%; height: 0; padding-top: 56.25%; 
                    box-shadow: 0 2px 8px rgba(63, 69, 81, 0.16); border-radius: 8px; overflow: hidden;">
            <iframe loading="lazy" style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; border: none;"
                src="https://www.canva.com/design/DAGW-eIO5CU/mXHtrsuUd4G5hsW73S9BKQ/view?embed" allowfullscreen>
            </iframe>
        </div>
        <p style="text-align: center; margin-top: 1em;">
            <a href="https://www.canva.com/design/DAGW-eIO5CU/mXHtrsuUd4G5hsW73S9BKQ/view" 
               target="_blank" rel="noopener" style="text-decoration: none; color: #2a7ae4; font-weight: bold;">
                Ver la presentación "Cuento 4" en Canva
            </a>
        </p>`
    }
};

// Función para abrir el modal y mostrar el contenido del cuento
function abrirModal(cuentoId) {
    const cuento = cuentos[cuentoId];
    document.getElementById('modal-body').innerHTML = `
        <h2>${cuento.titulo}</h2>
        ${cuento.contenido}
    `;
    document.getElementById('modal').style.display = 'block';
}

// Función para cerrar el modal
function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}
