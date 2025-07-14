-- Script para agregar columna de foto a la tabla ninos
-- Ejecutar este script en MySQL Workbench o phpMyAdmin

USE ashakids;

-- Agregar columna para almacenar la foto del niño
ALTER TABLE ninos ADD COLUMN foto LONGBLOB;

-- Agregar columna para el tipo MIME de la imagen
ALTER TABLE ninos ADD COLUMN tipo_foto VARCHAR(50) DEFAULT 'image/jpeg';

-- Verificar que se agregaron las columnas
DESCRIBE ninos;

-- Mostrar las columnas agregadas
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'ashakids' 
AND TABLE_NAME = 'ninos' 
AND COLUMN_NAME IN ('foto', 'tipo_foto'); 