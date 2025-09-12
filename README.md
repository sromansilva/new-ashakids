# 🧸 ASHAKids

Plataforma para la gestión de citas de atención temprana, notificaciones por correo y coordinación de sesiones vía Zoom.  

---

## 📑 Tabla de contenidos

1. [Descripción](#-descripción)  
2. [Prerrequisitos](#-prerrequisitos)  
3. [Instalación y configuración](#-instalación-y-configuración)  
   - [Clonar el repositorio](#clonar-el-repositorio)  
   - [Variables de entorno](#variables-de-entorno)  
   - [Levantar servicios](#levantar-servicios)  
4. [Estructura del proyecto](#-estructura-del-proyecto)  
5. [Endpoints principales](#-endpoints-principales)  
6. [Pruebas](#-pruebas)  
7. [Documentación viva](#-documentación-viva)  
8. [Cómo contribuir](#-cómo-contribuir)  
9. [Licencia](#-licencia)  
10. [Contacto](#-contacto)  

---

## 📖 Descripción

Este proyecto agrupa tres servicios principales:  

1. **backend-spring/**: API REST en Java con Spring Boot para gestionar usuarios, padres, terapeutas, citas y dashboards.  
2. **backend-mail/**: Scripts en PHP que envían correos de confirmación y boletines usando PHPMailer.  
3. **backend-zoom/**: Servicio en Node.js/Express que consume la API de Zoom para crear y gestionar reuniones.  

Además incluye **scripts SQL** para creación y migración de tablas.  

---

## 🛠 Prerrequisitos

Antes de empezar, asegúrate de tener instalados:  

- **Java 17+** y Maven  
- **PHP 7.4+**  
- **Node.js 16+** y npm  
- **MySQL 8+** (u otro RDBMS compatible)  
- **Git**  

---

## ⚙️ Instalación y configuración

### 📌 Clonar el repositorio

```bash
git clone https://github.com/SergioSK21/new-ashakids.git
cd new-ashakids

--- 

## 📌 Variables de entorno

Copia los archivos de ejemplo y completa tus credenciales:  

```bash
# Para Spring Boot
cp backend-spring/.env.example backend-spring/.env

# Para PHP-Mail
cp backend-mail/.env.example backend-mail/.env

# Para Node/Zoom
cp backend-zoom/.env.example backend-zoom/.env
---

🚀 Levantar servicios

En terminales separadas, ejecuta:

# API Spring Boot
cd backend-spring
mvn spring-boot:run

# Servicio PHP-Mail
cd backend-mail
php mailer.php

# Servicio Node/Zoom
cd backend-zoom
npm install
npm start


👉 Cada servicio escucha en el puerto configurado en su archivo .env.

---
📂 Estructura del proyecto
new-ashakids/
├─ backend-spring/       # Código Java/Spring Boot
├─ backend-mail/         # Scripts PHP + PHPMailer
├─ backend-zoom/         # Node.js/Express + Zoom API
├─ sql/                  # Scripts SQL de creación y migraciones
├─ docs/                 # Documentación adicional (diagramas, guías)
├─ .gitignore            # Excluir node_modules/, /target, .env, etc.
└─ README.md             # Este archivo

---
🔗 Endpoints principales
🌱 API Spring Boot

📂 Carpeta: backend-spring/src/main/java/.../controller/

Método	Ruta	Descripción
GET	/api/usuarios	Listar usuarios
POST	/api/citas	Crear nueva cita
GET	/api/dashboard	Datos para el panel administrativo
🎥 Servicio Node/Zoom

📂 Carpeta: backend-zoom/routes/

Método	Ruta	Descripción
POST	/zoom/meetings	Crear reunión en Zoom
📧 Servicio PHP-Mail

📂 Carpeta: backend-mail/

Punto de entrada: mailer.php

Parámetros esperados vía POST: email, template, datos

✅ Pruebas

🧪 Pruebas unitarias con JUnit en backend-spring/src/test/

🔎 Test de endpoints con Postman o cURL

📚 Documentación viva

En la carpeta docs/ encontrarás:

📊 Diagramas de arquitectura (Mermaid o Draw.io)

📖 Guía de contribución y estándares de código

✉️ Plantillas para diseño de emails

---

🤝 Cómo contribuir

Haz un fork del repositorio

Crea una nueva rama con tu feature

git checkout -b feature/nueva-funcion


Haz commit de tus cambios

git commit -m "Agrego nueva función"


Haz push a tu rama

git push origin feature/nueva-funcion


Crea un Pull Request

---

📜 Licencia

Este proyecto está bajo licencia MIT.
Consulta el archivo LICENSE
 para más detalles.

---

📧 Contacto

Proyecto: ASHAKids

Equipo: Universitarios UTP

Email: contacto@ashakids.com