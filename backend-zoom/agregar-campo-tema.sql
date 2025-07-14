-- Agregar campo tema a la tabla citas
ALTER TABLE citas ADD COLUMN tema VARCHAR(255) AFTER modalidad;

-- Verificar que se agregó correctamente
DESCRIBE citas; 