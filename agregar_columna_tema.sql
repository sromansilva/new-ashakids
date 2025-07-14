-- Script para agregar la columna tema a la tabla citas
USE Ashakids;

-- Agregar columna tema a la tabla citas
ALTER TABLE citas ADD COLUMN tema VARCHAR(255) AFTER retroalimentacion_padre;

-- Verificar que la columna se agregó correctamente
DESCRIBE citas; 