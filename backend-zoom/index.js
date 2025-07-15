const express = require("express");
const axios = require("axios");
require("dotenv").config();
const cors = require('cors'); // Para habilitar CORS
const mysql = require('mysql2/promise'); // Para conectar con la base de datos

const app = express();
app.use(express.json());
app.use(express.static('public'));
app.use(cors());  // Habilitar CORS

// Configuración de la base de datos
const dbConfig = {
    host: 'localhost',
    user: 'root',
<<<<<<< HEAD
    password: 'Hola.1234',
=======
    password: 'Mantequillademani1%',
>>>>>>> fbd929775b0e1d52fb65a88f83da5d9d5a4f80c7
    database: 'Ashakids'
};

// Obtener access_token automáticamente desde Zoom
async function getAccessToken() {
    try {
        const response = await axios.post("https://zoom.us/oauth/token", null, {
            params: {
                grant_type: "account_credentials",
                account_id: process.env.ZOOM_ACCOUNT_ID
            },
            auth: {
                username: process.env.ZOOM_CLIENT_ID,
                password: process.env.ZOOM_CLIENT_SECRET
            }
        });

        return response.data.access_token;
    } catch (err) {
        console.error("Error al obtener el access_token:", err.response?.data || err);
        throw new Error('No se pudo obtener el token de acceso.');
    }
}

// Obtener terapeutas disponibles
app.get("/terapeutas", async (req, res) => {
    try {
        const db = await mysql.createPool(dbConfig);
        const [rows] = await db.query(
            'SELECT id_usuario, nombre, especialidad FROM usuarios WHERE rol = "terapeuta" AND disponible = true'
        );
        res.json(rows);
    } catch (err) {
        console.error("Error al obtener terapeutas:", err);
        res.status(500).json({ error: "No se pudieron obtener los terapeutas" });
    }
});

// Guardar cita en la base de datos
app.post("/guardar-cita", async (req, res) => {
    try {
        const { idNino, idTerapeuta, fecha, hora, modalidad, enlace_o_direccion, tema, nombreTutor } = req.body;
        
        console.log('Datos recibidos:', { idNino, idTerapeuta, fecha, hora, modalidad, enlace_o_direccion, tema, nombreTutor });
        
        const db = await mysql.createPool(dbConfig);
        
        const query = 'INSERT INTO citas (id_nino, id_terapeuta, fecha, hora, modalidad, enlace_o_direccion, estado, retroalimentacion_padre, tema) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)';
        const params = [idNino, idTerapeuta, fecha, hora, modalidad, enlace_o_direccion || null, 'programada', nombreTutor || null, tema || null];
        
        console.log('Query:', query);
        console.log('Parámetros:', params);
        
        const [result] = await db.query(query, params);
        
        console.log('Resultado:', result);
        
        res.json({ 
            mensaje: "Cita guardada exitosamente", 
            id_cita: result.insertId 
        });
    } catch (err) {
        console.error("Error al guardar cita:", err);
        console.error("Detalles del error:", err.message);
        res.status(500).json({ error: "No se pudo guardar la cita", details: err.message });
    }
});

// Cancelar cita (eliminar de la base de datos)
app.delete('/cancelar-cita/:idCita', async (req, res) => {
    try {
        const { idCita } = req.params;
        
        const db = await mysql.createPool(dbConfig);
        
        const [result] = await db.query(
            'DELETE FROM citas WHERE id_cita = ?',
            [idCita]
        );
        
        if (result.affectedRows > 0) {
            res.json({ 
                mensaje: "Cita cancelada exitosamente" 
            });
        } else {
            res.status(404).json({ error: "Cita no encontrada" });
        }
    } catch (err) {
        console.error('Error al cancelar cita:', err);
        res.status(500).json({ error: "No se pudo cancelar la cita" });
    }
});

// Crear reunión Zoom
app.post("/crear-reunion", async (req, res) => {
    try {
        const token = await getAccessToken();

        const { topic, start_time, duration, agenda } = req.body;

        const response = await axios.post(
            "https://api.zoom.us/v2/users/me/meetings",
            {
                topic: topic || "Sesión de Terapia ASHAKids",  // Si no se pasa un tema, usa el default
                type: 2, // reunión programada
                start_time: req.body.start_time,
                duration: duration,
                timezone: "America/Lima",
                settings: {
                    join_before_host: true
                }
            },
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            }
        );

        const link = response.data.join_url;
        res.json({ mensaje: "Reunión creada", linkZoom: link });

    } catch (err) {
        console.error("Error al crear reunión:", err.response?.data || err);
        res.status(500).json({ mensaje: "No se pudo crear la reunión.", error: err.message });
    }
});

// Reprogramar cita
app.put('/reprogramar-cita', async (req, res) => {
    try {
        const { idCita, fecha, hora, modalidad, tema } = req.body;
        
        const db = await mysql.createPool(dbConfig);
        
        let enlace_o_direccion = null;
        
        // Si es virtual, crear nueva reunión Zoom
        if (modalidad === 'Virtual') {
            try {
                const token = await getAccessToken();
                const response = await axios.post(
                    "https://api.zoom.us/v2/users/me/meetings",
                    {
                        topic: tema || "Sesión de Terapia ASHAKids",
                        type: 2,
                        start_time: `${fecha}T${hora}:00`,
                        duration: 60,
                        timezone: "America/Lima",
                        settings: {
                            join_before_host: true
                        }
                    },
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "application/json"
                        }
                    }
                );
                enlace_o_direccion = response.data.join_url;
            } catch (zoomError) {
                console.error('Error al crear reunión Zoom:', zoomError);
                enlace_o_direccion = 'Error al crear enlace Zoom';
            }
        } else {
            enlace_o_direccion = 'Sede AshaKids';
        }
        
        const [result] = await db.query(`
            UPDATE citas 
            SET fecha = ?, hora = ?, modalidad = ?, tema = ?, enlace_o_direccion = ?, estado = 'reprogramada'
            WHERE id_cita = ?
        `, [fecha, hora, modalidad, tema, enlace_o_direccion, idCita]);
        
        if (result.affectedRows > 0) {
            res.json({ 
                mensaje: "Cita reprogramada exitosamente",
                enlace_o_direccion: enlace_o_direccion
            });
        } else {
            res.status(404).json({ error: "Cita no encontrada" });
        }
    } catch (err) {
        console.error('Error al reprogramar cita:', err);
        res.status(500).json({ error: "No se pudo reprogramar la cita" });
    }
});

// Obtener niños de un padre específico
app.get('/ninos/:idPadre', async (req, res) => {
    try {
        const db = await mysql.createPool(dbConfig);
        const [rows] = await db.query(
            'SELECT id_nino, nombre, fecha_nacimiento FROM ninos WHERE id_padre = ?',
            [req.params.idPadre]
        );
        res.json(rows);
    } catch (err) {
        res.status(500).json({ error: 'No se pudieron obtener los niños' });
    }
});

// Obtener citas de un padre específico
app.get('/citas-padre/:idPadre', async (req, res) => {
    try {
        const db = await mysql.createPool(dbConfig);
        const [rows] = await db.query(`
            SELECT c.*, n.nombre as nombre_nino, u.nombre as nombre_terapeuta 
            FROM citas c 
            JOIN ninos n ON c.id_nino = n.id_nino 
            JOIN usuarios u ON c.id_terapeuta = u.id_usuario 
            WHERE n.id_padre = ? 
            ORDER BY c.fecha ASC, c.hora ASC
        `, [req.params.idPadre]);
        
        res.json(rows);
    } catch (err) {
        console.error('Error al obtener citas del padre:', err);
        res.status(500).json({ error: 'No se pudieron obtener las citas' });
    }
});

app.listen(3000, () => {
    console.log("Servidor en http://localhost:3000");
});
