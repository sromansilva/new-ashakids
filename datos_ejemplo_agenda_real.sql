-- Script para agregar datos de ejemplo reales para la agenda del padre
-- Ejecutar después de tener la estructura de base de datos completa

-- Insertar padre de ejemplo
INSERT INTO usuarios (id_usuario, nombre, email, password, rol) VALUES
(1, 'Juan Pérez', 'juan.perez@email.com', 'password123', 'padre')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- Insertar niños de ejemplo para el padre con ID 1
INSERT INTO ninos (id_nino, nombre, fecha_nacimiento, id_padre) VALUES
(1, 'Juan Pérez Jr.', '2016-05-15', 1),
(2, 'María Pérez', '2018-03-22', 1)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- Insertar terapeutas de ejemplo
INSERT INTO usuarios (id_usuario, nombre, email, password, rol, especialidad, disponible) VALUES
(10, 'Dr. Ana García', 'ana.garcia@ashakids.com', 'password123', 'terapeuta', 'Terapia del Lenguaje', true),
(11, 'Lic. Carlos López', 'carlos.lopez@ashakids.com', 'password123', 'terapeuta', 'Terapia Ocupacional', true),
(12, 'Psic. Laura Martínez', 'laura.martinez@ashakids.com', 'password123', 'terapeuta', 'Psicología Infantil', true)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- Insertar citas de ejemplo reales
INSERT INTO citas (id_cita, id_nino, id_terapeuta, fecha, hora, modalidad, enlace_o_direccion, estado, tema, retroalimentacion_padre) VALUES
(1, 1, 10, '2024-01-15', '10:00:00', 'Virtual', 'https://meet.google.com/abc-defg-hij', 'programada', 'Terapia del lenguaje', 'Padre'),
(2, 2, 11, '2024-01-18', '15:30:00', 'Presencial', 'Sede AshaKids', 'programada', 'Ejercicios motores', 'Padre'),
(3, 1, 12, '2024-01-10', '14:00:00', 'Virtual', 'https://meet.google.com/xyz-uvw-rst', 'completada', 'Evaluación psicológica', 'Padre'),
(4, 2, 10, '2024-01-20', '09:00:00', 'Virtual', 'https://meet.google.com/def-ghi-jkl', 'reprogramada', 'Terapia del lenguaje', 'Padre'),
(5, 1, 11, '2024-01-25', '11:00:00', 'Presencial', 'Sede AshaKids', 'programada', 'Terapia ocupacional', 'Padre')
ON DUPLICATE KEY UPDATE 
    fecha = VALUES(fecha),
    hora = VALUES(hora),
    modalidad = VALUES(modalidad),
    estado = VALUES(estado),
    tema = VALUES(tema);

-- Verificar que los datos se insertaron correctamente
SELECT 
    c.id_cita,
    n.nombre as nombre_nino,
    u.nombre as nombre_terapeuta,
    c.fecha,
    c.hora,
    c.modalidad,
    c.estado,
    c.tema,
    c.enlace_o_direccion
FROM citas c
JOIN ninos n ON c.id_nino = n.id_nino
JOIN usuarios u ON c.id_terapeuta = u.id_usuario
WHERE n.id_padre = 1
ORDER BY c.fecha ASC, c.hora ASC; 