# ASHAKids

Plataforma para la gestión de citas de atención temprana, notificaciones por correo y coordinación de sesiones vía Zoom.

## Tabla de contenidos

1. [Descripción](#descripción)
2. [Prerrequisitos](#prerrequisitos)
3. [Instalación y configuración](#instalación-y-configuración)

   * [Clonar el repositorio](#clonar-el-repositorio)
   * [Variables de entorno](#variables-de-entorno)
   * [Levantar servicios](#levantar-servicios)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Endpoints principales](#endpoints-principales)
6. [Documentación viva](#documentación-viva)
7. [Cómo contribuir](#cómo-contribuir)
8. [Licencia](#licencia)
9. [Contacto](#contacto)

---

## Descripción

Este proyecto agrupa tres servicios:

1. **backend-spring/**: API REST en Java con Spring Boot para gestionar usuarios, padres, terapeutas, citas y dashboards.
2. **backend-mail/**: Scripts en PHP que envían correos de confirmación y boletines usando PHPMailer.
3. **backend-zoom/**: Servicio en Node.js/Express que consume la API de Zoom para crear y gestionar reuniones.

Además incluye scripts SQL para creación y migración de tablas.

---

## Prerrequisitos

Antes de empezar, asegúrate de tener instalados:

* **Java 17+** y Maven
* **PHP 7.4+**
* **Node.js 16+** y npm
* **MySQL 8+** (u otro RDBMS compatible)
* **Git**

---

## Instalación y configuración

### Clonar el repositorio

```bash
git clone https://github.com/SergioSK21/new-ashakids.git
cd new-ashakids
```

### Variables de entorno

Copia el archivo de ejemplo y completa tus credenciales:

```bash
# Para Spring Boot
cp backend-spring/.env.example backend-spring/.env
# Para PHP-Mail
cp backend-mail/.env.example backend-mail/.env
# Para Node/Zoom
cp backend-zoom/.env.example backend-zoom/.env
```

Contenido típico de `.env`:

```
# backend-spring/.env
DB_URL=jdbc:mysql://localhost:3306/ashakids
DB_USER=root
DB_PASSWORD=secreto
JWT_SECRET=supersecreto

# backend-mail/.env
SMTP_HOST=smtp.ejemplo.com
SMTP_USER=usuario
SMTP_PASS=clave

# backend-zoom/.env
ZOOM_API_KEY=tu_zoom_api_key
ZOOM_API_SECRET=tu_zoom_api_secret
```

### Levantar servicios

En terminales separadas, ejecuta:

```bash
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
```

> Cada uno escuchará en su puerto configurado en `.env`.

---

## Estructura del proyecto

```plaintext
new-ashakids/
├─ backend-spring/       # Código Java/Spring Boot
├─ backend-mail/         # Scripts PHP + PHPMailer
├─ backend-zoom/         # Node.js/Express + Zoom API
├─ sql/                  # Scripts SQL de creación y migraciones
├─ docs/                 # Documentación adicional (diagramas, guías)
├─ .gitignore            # Excluir `node_modules/`, `/target`, `.env`, etc.
└─ README.md             # Este archivo
```

---

## Endpoints principales

### API Spring Boot

| Método | Ruta             | Descripción                        |
| ------ | ---------------- | ---------------------------------- |
| GET    | `/api/usuarios`  | Listar usuarios                    |
| POST   | `/api/citas`     | Crear nueva cita                   |
| GET    | `/api/dashboard` | Datos para el panel administrativo |

### Servicio Node/Zoom

| Métod | Ruta             | Descripción           |
| ----- | ---------------- | --------------------- |
| POST  | `/zoom/meetings` | Crear reunión en Zoom |

### PHP-Mail

* Punto de entrada: `mailer.php`
* Parámetros esperados vía `POST`: `email`, `template`, `datos`.

---
## Documentación viva

En la carpeta `docs/` encontrarás:

* Diagramas de arquitectura (Mermaid o draw\.io).
* Guía de contribución y estándares de código.
* Plantillas para diseño de emails.
---

## Licencia

Este proyecto está bajo licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.


