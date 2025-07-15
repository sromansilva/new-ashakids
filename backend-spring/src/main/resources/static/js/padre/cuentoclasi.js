// Datos de los cuentos
const cuentos = {
    cuento1: {
        titulo: "Ricitos de Oro y los tres osos",
        contenido: `
        <div style="position: relative; width: 100%; height: 0; padding-top: 56.25%; 
                    box-shadow: 0 2px 8px rgba(63, 69, 81, 0.16); border-radius: 8px; overflow: hidden;">
            <iframe loading="lazy" style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; border: none;"
                src="https://www.canva.com/design/DAGWhLZSw_Q/i8N8_zacrsr9s1symIoBJg/view?embed" allowfullscreen>
            </iframe>
        </div>
        <p style="text-align: center; margin-top: 1em;">
            <a href="https://www.canva.com/design/DAGWhLZSw_Q/i8N8_zacrsr9s1symIoBJg/view?utm_content=DAGWhLZSw_Q&utm_campaign=designshare&utm_medium=embeds&utm_source=link" 
               target="_blank" rel="noopener" style="text-decoration: none; color: #2a7ae4; font-weight: bold;">
                Ver la presentación "Goldilocks and the Three Bears" en Canva
            </a>
        </p>`

    },
    cuento2: {
        titulo: "Los 3 Cerditos",
        contenido: `
        <div style="position: relative; width: 100%; height: 0; padding-top: 56.25%; 
                    box-shadow: 0 2px 8px rgba(63, 69, 81, 0.16); border-radius: 8px; overflow: hidden;">
            <iframe loading="lazy" style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; border: none;"
                src="https://www.canva.com/design/DAGWgqi8_-w/w8CyM9JBzZh5uQfNl3ypVg/view?embed" allowfullscreen>
            </iframe>
        </div>
        <p style="text-align: center; margin-top: 1em;">
            <a href="https://www.canva.com/design/DAGWgqi8_-w/w8CyM9JBzZh5uQfNl3ypVg/view" 
               target="_blank" rel="noopener" style="text-decoration: none; color: #2a7ae4; font-weight: bold;">
                Ver la presentación "Three Little Pigs" en Canva
            </a>
        </p>`
    },
    cuento3: {
        titulo: "Jack y las Habichuelas Mágicas",
        contenido: ` <div style="position: relative; width: 100%; height: 0; padding-top: 56.2500%;
        padding-bottom: 0; box-shadow: 0 2px 8px 0 rgba(63,69,81,0.16); margin-top: 1.6em; margin-bottom: 0.9em; overflow: hidden;
        border-radius: 8px; will-change: transform;">
         <iframe loading="lazy" style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; border: none; padding: 0;margin: 0;"
           src="https://www.canva.com/design/DAGWqqFbIyo/o9507B1YZDxb-EhARiBKww/view?embed" allowfullscreen="allowfullscreen" allow="fullscreen">
         </iframe>
       </div>
       <p style="text-align: center; margin-top: 1em;">
       <a href="https:&#x2F;&#x2F;www.canva.com&#x2F;design&#x2F;DAGWqqFbIyo&#x2F;o9507B1YZDxb-EhARiBKww&#x2F;view?utm_content=DAGWqqFbIyo&amp;utm_campaign=designshare&amp;utm_medium=embeds&amp;utm_source=link" target="_blank" rel="noopener">Jack and the Beanstalk Education presentation in Colorful Animated Style </a>
       </p>`
    },
    cuento4: {
        titulo: "Hansel y Gretel",
        contenido:
            ` <div style="position: relative; width: 100%; height: 0; padding-top: 56.2500%;
        padding-bottom: 0; box-shadow: 0 2px 8px 0 rgba(63,69,81,0.16); margin-top: 1.6em; margin-bottom: 0.9em; overflow: hidden;
        border-radius: 8px; will-change: transform;">
         <iframe loading="lazy" style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; border: none; padding: 0;margin: 0;"
           src="https://www.canva.com/design/DAGWqrn-6bw/ARCvM8ZrjUiHzipa_2_pGw/view?embed" allowfullscreen="allowfullscreen" allow="fullscreen">
         </iframe>
       </div>
       <p style="text-align: center; margin-top: 1em;">
       <a href="https:&#x2F;&#x2F;www.canva.com&#x2F;design&#x2F;DAGWqrn-6bw&#x2F;ARCvM8ZrjUiHzipa_2_pGw&#x2F;view?utm_content=DAGWqrn-6bw&amp;utm_campaign=designshare&amp;utm_medium=embeds&amp;utm_source=link" target="_blank" rel="noopener">Los Duendes y el Zapatero Presentación educativa Verde Marrón Divertido</a>
       </p>`
    },

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