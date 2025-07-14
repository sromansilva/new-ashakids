USE Ashakids;

-- Actualizar citas existentes que no tienen tema
UPDATE citas SET tema = 'Cita de Terapia' WHERE tema IS NULL OR tema = '';

-- Verificar que se actualizaron
SELECT id_cita, tema, fecha, hora FROM citas; 