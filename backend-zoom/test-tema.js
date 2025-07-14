// Script de prueba para verificar que el tema se está enviando correctamente
const axios = require('axios');

async function testGuardarCita() {
    try {
        const datosCita = {
            idNino: 1,
            idTerapeuta: 2,
            fecha: '2025-01-20',
            hora: '10:00',
            modalidad: 'Virtual',
            enlace_o_direccion: 'https://zoom.us/j/123456789',
            nombreTutor: 'María González',
            tema: 'Evaluación del Lenguaje'
        };

        console.log('Enviando datos de cita:', datosCita);

        const response = await axios.post('http://localhost:3000/guardar-cita', datosCita);
        
        console.log('Respuesta del servidor:', response.data);
        
        if (response.data.mensaje) {
            console.log('✅ Cita guardada exitosamente');
            console.log('ID de la cita:', response.data.id_cita);
        }
    } catch (error) {
        console.error('❌ Error al guardar cita:', error.response?.data || error.message);
    }
}

testGuardarCita(); 