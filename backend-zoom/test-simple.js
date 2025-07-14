// Script simple para probar la conexión
const axios = require('axios');

async function testSimple() {
    try {
        console.log('🧪 Probando conexión...');
        
        // Probar conexión básica
        const response = await axios.get('http://localhost:3000/terapeutas');
        console.log('✅ Conexión exitosa, terapeutas encontrados:', response.data.length);
        
        // Probar guardar una cita simple
        const citaSimple = {
            idNino: 1,
            idTerapeuta: 2,
            fecha: '2025-01-26',
            hora: '15:00',
            modalidad: 'Presencial',
            enlace_o_direccion: null,
            nombreTutor: 'Test Tutor',
            tema: 'Test Tema'
        };
        
        console.log('📝 Guardando cita de prueba...');
        const saveResponse = await axios.post('http://localhost:3000/guardar-cita', citaSimple);
        console.log('✅ Respuesta del servidor:', saveResponse.data);
        
    } catch (error) {
        console.error('❌ Error:', error.response?.data || error.message);
        if (error.response?.data?.details) {
            console.error('📋 Detalles del error:', error.response.data.details);
        }
    }
}

testSimple(); 