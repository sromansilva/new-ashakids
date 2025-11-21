#!/usr/bin/env python3
"""
Script para agregar automáticamente el atributo data-role
a las páginas HTML del proyecto ASHAKids.

Este script busca archivos HTML en las carpetas padre/ y terapeuta/
y agrega el atributo data-role correspondiente al elemento <body>.
"""

import os
import re
from pathlib import Path

# Rutas de los templates
BASE_PATH = Path(__file__).parent / 'src' / 'main' / 'resources' / 'templates'

PADRE_PATH = BASE_PATH / 'padre'
TERAPEUTA_PATH = BASE_PATH / 'terapeuta'

def add_data_role_to_file(file_path, role):
    """
    Agrega el atributo data-role al elemento <body> de un archivo HTML.
    
    Args:
        file_path: Ruta al archivo HTML
        role: Rol a agregar ('PADRE' o 'TERAPEUTA')
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Buscar <body> o <body> con espacios y otros atributos
        patterns = [
            (r'<body>', f'<body data-role="{role}">'),
            (r'<body\s+>', f'<body data-role="{role}">'),
            (r'<body\s+([^>]*?)(\s*)>', rf'<body \1 data-role="{role}"\2>'),
        ]
        
        modified = False
        for pattern, replacement in patterns:
            if re.search(pattern, content):
                # Verificar si ya tiene data-role
                if 'data-role' not in content:
                    content = re.sub(pattern, replacement, content)
                    modified = True
                    break
        
        if modified:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f'✅ Actualizado: {file_path.name} (rol: {role})')
            return True
        else:
            if 'data-role' in content:
                print(f'⚠️  Ya tiene data-role: {file_path.name}')
            else:
                print(f'❌ No se pudo actualizar: {file_path.name}')
            return False
    
    except Exception as e:
        print(f'❌ Error al procesar {file_path.name}: {e}')
        return False

def main():
    """Función principal"""
    print('🤖 Agregando atributos data-role para el chatbot ASHAKids\n')
    
    # Procesar páginas de PADRE
    if PADRE_PATH.exists():
        print('📁 Procesando páginas de PADRE...')
        padre_files = list(PADRE_PATH.glob('*.html'))
        for file_path in padre_files:
            add_data_role_to_file(file_path, 'PADRE')
        print(f'   Total: {len(padre_files)} archivos\n')
    
    # Procesar páginas de TERAPEUTA
    if TERAPEUTA_PATH.exists():
        print('📁 Procesando páginas de TERAPEUTA...')
        terapeuta_files = list(TERAPEUTA_PATH.glob('*.html'))
        for file_path in terapeuta_files:
            add_data_role_to_file(file_path, 'TERAPEUTA')
        print(f'   Total: {len(terapeuta_files)} archivos\n')
    
    print('✨ Proceso completado!')
    print('\n💡 Nota: El chatbot funcionará incluso sin data-role,')
    print('   ya que detecta automáticamente el rol desde la URL.')

if __name__ == '__main__':
    main()

