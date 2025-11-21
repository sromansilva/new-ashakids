# 🤖 Chatbot ASHAKids - Guía de Integración

Este documento explica cómo integrar el chatbot flotante en las páginas del proyecto ASHAKids.

## 📁 Archivos del Chatbot

El chatbot está compuesto por tres archivos principales:

1. **`/static/css/chatbot.css`** - Estilos del chatbot y botón de WhatsApp
2. **`/static/js/chatbot.js`** - Lógica JavaScript del chatbot
3. **`/templates/fragments/chatbot.html`** - Fragmento Thymeleaf (opcional, para referencia)

## 🚀 Integración Rápida

### Paso 1: Agregar CSS en el `<head>`

Agrega el siguiente enlace CSS en la sección `<head>` de tu layout:

```html
<link rel="stylesheet" th:href="@{/css/chatbot.css}">
```

### Paso 2: Agregar JavaScript antes de `</body>`

Agrega el siguiente script antes del cierre de `</body>`:

```html
<script th:src="@{/js/chatbot.js}"></script>
```

### Paso 3: Configurar el Rol en el `<body>`

Agrega el atributo `data-role` al elemento `<body>` según el usuario:

**Para páginas de PADRE:**
```html
<body data-role="PADRE">
```

**Para páginas de TERAPEUTA:**
```html
<body data-role="TERAPEUTA">
```

## 📝 Ejemplo Completo de Integración

### Para Layout de Padre (`padreLayout.html`)

```html
<head th:fragment="Head">
  <!-- ... otros enlaces ... -->
  
  <!-- CSS del Chatbot -->
  <link rel="stylesheet" th:href="@{/css/chatbot.css}">
</head>

<body data-role="PADRE">
  <!-- ... contenido de la página ... -->
  
  <!-- Scripts -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script th:src="@{/js/padre/padres.js}"></script>
  
  <!-- JavaScript del Chatbot -->
  <script th:src="@{/js/chatbot.js}"></script>
</body>
```

### Para Layout de Terapeuta (`terapeutaLayout.html`)

```html
<head th:fragment="Head">
  <!-- ... otros enlaces ... -->
  
  <!-- CSS del Chatbot -->
  <link rel="stylesheet" th:href="@{/css/chatbot.css}">
</head>

<body data-role="TERAPEUTA">
  <!-- ... contenido de la página ... -->
  
  <!-- Scripts -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script th:src="@{/js/terapeuta/terapeuta.js}"></script>
  
  <!-- JavaScript del Chatbot -->
  <script th:src="@{/js/chatbot.js}"></script>
</body>
```

## ⚙️ Configuración

### Cambiar el Número de WhatsApp

Para cambiar el número de WhatsApp, edita el archivo `chatbot.js`:

```javascript
const CONFIG = {
  whatsappNumber: '51987654321', // Cambia este número
  // ...
};
```

**Formato del número:**
- Código de país sin el signo `+`
- Número sin espacios ni guiones
- Ejemplo: `51987654321` (Perú) o `521234567890` (México)

### Personalizar Preguntas y Respuestas

Edita el archivo `chatbot.js` en la sección `CONFIG`:

```javascript
const CONFIG = {
  roles: {
    PADRE: {
      questions: [
        'Ver mis citas',
        'Agendar una cita',
        // Agrega más preguntas aquí
      ],
      responses: {
        'Ver mis citas': 'Tu respuesta personalizada aquí',
        // Agrega más respuestas aquí
      }
    },
    TERAPEUTA: {
      // Similar estructura
    }
  }
};
```

## 🎨 Personalización de Estilos

Los estilos están en `chatbot.css`. Puedes personalizar:

- **Colores**: Busca las clases `.chatbot-float-btn`, `.chatbot-header`, etc.
- **Tamaños**: Ajusta `width` y `height` en las clases correspondientes
- **Animaciones**: Modifica las transiciones y keyframes (`@keyframes`)

## 🔍 Detección Automática del Rol

El chatbot intenta detectar el rol automáticamente si no encuentra `data-role`:

1. Primero busca el atributo `data-role` en el `<body>`
2. Si no existe, analiza la URL:
   - URLs que contengan `/padre` o `/padre/` → Rol: PADRE
   - URLs que contengan `/terapeuta` o `/terapeuta/` → Rol: TERAPEUTA
3. Si no encuentra nada, usa PADRE como valor por defecto

## 📱 Responsive

El chatbot es completamente responsive:

- **Desktop**: Ventana de 380px de ancho
- **Tablet**: Se adapta al ancho de la pantalla
- **Mobile**: Ventana completa menos márgenes (40px)

## 🐛 Solución de Problemas

### El chatbot no aparece

1. Verifica que los archivos CSS y JS estén cargando correctamente
2. Abre la consola del navegador (F12) y busca errores
3. Verifica que el JavaScript se esté ejecutando después de que el DOM esté listo

### El rol no se detecta correctamente

1. Asegúrate de agregar `data-role="PADRE"` o `data-role="TERAPEUTA"` en el `<body>`
2. Verifica en la consola el mensaje: `ASHAKids Chatbot inicializado con rol: ...`

### Los estilos no se aplican

1. Verifica que el CSS esté cargando después de Bootstrap
2. Revisa que no haya conflictos con otros estilos
3. Inspecciona el elemento en el navegador para ver qué estilos se están aplicando

## ✨ Características

- ✅ 100% HTML, CSS y JavaScript puro (sin dependencias externas)
- ✅ Diseño infantil con colores pastel
- ✅ Animaciones suaves de apertura/cierre
- ✅ Botón flotante de WhatsApp integrado
- ✅ Preguntas diferentes según el rol del usuario
- ✅ Respuestas simuladas (sin backend)
- ✅ Responsive y compatible con móviles
- ✅ Código modular y fácil de mantener

## 📞 Soporte

Para cualquier duda o problema con la integración del chatbot, consulta este documento o revisa los comentarios en el código fuente.

---

**Versión:** 1.0  
**Fecha:** Enero 2025  
**Proyecto:** ASHAKids

