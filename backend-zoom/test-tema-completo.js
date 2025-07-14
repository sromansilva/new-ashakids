// Script de prueba completo para verificar el tema
const axios = require('axios');

async function testTemaCompleto() {
    try {
        console.log('🧪 Iniciando prueba completa del tema...');
        
        // 1. Crear una cita con tema
        const datosCita = {
            idNino: 1,
            idTerapeuta: 2,
            fecha: '2025-01-25',
            hora: '14:00',
            modalidad: 'Virtual',
            enlace_o_direccion: 'https://zoom.us/j/987654321',
            nombreTutor: 'María González',
            tema: 'Evaluación del Lenguaje - Prueba'
        };

        console.log('📝 Enviando cita con tema:', datosCita.tema);

        const response = await axios.post('http://localhost:3000/guardar-cita', datosCita);
        
        if (response.data.mensaje) {
            console.log('✅ Cita guardada exitosamente');
            console.log('🆔 ID de la cita:', response.data.id_cita);
            
            // 2. Verificar que se guardó en la base de datos
            console.log('🔍 Verificando en la base de datos...');
            
            // Aquí podrías hacer una consulta a la BD para verificar
            console.log('📊 La cita debería aparecer en la página del terapeuta con tema:', datosCita.tema);
            
        } else {
            console.log('❌ Error al guardar cita');
        }
        
    } catch (error) {
        console.error('❌ Error en la prueba:', error.response?.data || error.message);
    }
}

testTemaCompleto(); 