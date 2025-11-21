/**
 * ============================================
 * CHATBOT WIDGET - ASHAKids
 * Chatbot simple con JavaScript puro
 * ============================================
 */

(function() {
  'use strict';

  // Configuración del chatbot
  const CONFIG = {
    whatsappNumber: '51986309426', // Número de WhatsApp placeholder (formato: código país + número)
    roles: {
      PADRE: {
        questions: [
          'Ver mis citas',
          'Agendar una cita',
          'Ver recomendaciones de terapia',
          'Hablar con soporte'
        ],
        responses: {
          'Ver mis citas': '¡Por supuesto! Te puedo ayudar a ver tus citas. Puedes acceder a tu agenda desde el menú lateral o haciendo clic en "Agenda" en el dashboard. ¿Necesitas ayuda con algo más?',
          'Agendar una cita': 'Para agendar una cita, puedes hacer clic en el botón "Agendar reunión Zoom" en tu dashboard o ir directamente a la sección de Agenda. ¿Quieres que te guíe paso a paso?',
          'Ver recomendaciones de terapia': 'Las recomendaciones de terapia están disponibles en tu dashboard. También puedes consultar con tu terapeuta asignado para recomendaciones personalizadas. ¿Hay algo específico que te interese?',
          'Hablar con soporte': 'Puedes contactar con nuestro equipo de soporte a través del botón de WhatsApp que ves aquí al lado, o enviar un mensaje desde la sección de Mensajes. Estamos aquí para ayudarte. 😊'
        }
      },
      TERAPEUTA: {
        questions: [
          'Ver mis pacientes',
          'Ver mis citas de hoy',
          'Confirmar/rechazar citas',
          'Actualizar disponibilidad'
        ],
        responses: {
          'Ver mis pacientes': 'Puedes ver todos tus pacientes desde la sección "Pacientes" en el menú lateral. Allí encontrarás información detallada de cada uno. ¿Necesitas ayuda con algo específico?',
          'Ver mis citas de hoy': 'Tus citas del día las puedes ver en la sección "Agenda". Ahí encontrarás todas las citas programadas para hoy con los detalles de cada paciente. ¡Buena suerte con tus sesiones! 😊',
          'Confirmar/rechazar citas': 'Para confirmar o rechazar citas, ve a la sección "Agenda" y allí podrás gestionar cada cita. Recuerda confirmar las citas con anticipación. ¿Hay algo más en lo que pueda ayudarte?',
          'Actualizar disponibilidad': 'Para actualizar tu disponibilidad, ve a tu perfil de terapeuta desde el menú. Allí podrás modificar tus horarios y días disponibles. ¿Necesitas ayuda con algo más?'
        }
      }
    },
    defaultResponse: 'Gracias por tu consulta. ¿Hay algo más en lo que pueda ayudarte? Puedes seleccionar otra opción de las disponibles. 😊'
  };

  // Estado del chatbot
  let isOpen = false;
  let currentRole = 'PADRE'; // Valor por defecto

  /**
   * Obtiene el rol del usuario desde el atributo data-role del body
   */
  function getRoleFromBody() {
    const body = document.body;
    const role = body.getAttribute('data-role');
    
    if (role && CONFIG.roles[role.toUpperCase()]) {
      return role.toUpperCase();
    }
    
    // Intenta detectar el rol desde la URL o clases del body
    const path = window.location.pathname.toLowerCase();
    if (path.includes('/padre') || path.includes('/padre/')) {
      return 'PADRE';
    } else if (path.includes('/terapeuta') || path.includes('/terapeuta/')) {
      return 'TERAPEUTA';
    }
    
    return 'PADRE'; // Rol por defecto
  }

  /**
   * Inicializa el chatbot
   */
  function init() {
    currentRole = getRoleFromBody();
    createChatbotHTML();
    attachEventListeners();
    
    console.log('ASHAKids Chatbot inicializado con rol:', currentRole);
  }

  /**
   * Crea el HTML del chatbot
   */
  function createChatbotHTML() {
    // Contenedor de botones flotantes
    const floatContainer = document.createElement('div');
    floatContainer.className = 'chatbot-float-container';
    
    // Botón de WhatsApp
    const whatsappBtn = document.createElement('button');
    whatsappBtn.className = 'whatsapp-float-btn';
    whatsappBtn.setAttribute('aria-label', 'Contactar por WhatsApp');
    whatsappBtn.innerHTML = `
      <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <path d="M17.472 14.382c-.297-.149-1.758-.867-2.03-.967-.273-.099-.471-.148-.67.15-.197.297-.767.966-.94 1.164-.173.199-.347.223-.644.075-.297-.15-1.255-.463-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.298-.347.446-.52.149-.174.198-.298.298-.497.099-.198.05-.371-.025-.52-.075-.149-.669-1.612-.916-2.207-.242-.579-.487-.5-.669-.51-.173-.008-.371-.01-.57-.01-.198 0-.52.074-.792.372-.272.297-1.04 1.016-1.04 2.479 0 1.462 1.065 2.875 1.213 3.074.149.198 2.096 3.2 5.077 4.487.709.306 1.262.489 1.694.625.712.227 1.36.195 1.871.118.571-.085 1.758-.719 2.006-1.413.248-.694.248-1.289.173-1.413-.074-.124-.272-.198-.57-.347m-5.421 7.403h-.004a9.87 9.87 0 01-5.031-1.378l-.361-.214-3.741.982.998-3.648-.235-.374a9.86 9.86 0 01-1.51-5.26c.001-5.45 4.436-9.884 9.888-9.884 2.64 0 5.122 1.03 6.988 2.898a9.825 9.825 0 012.893 6.994c-.003 5.45-4.437 9.884-9.885 9.884m8.413-18.297A11.815 11.815 0 0012.05 0C5.495 0 .16 5.335.157 11.892c0 2.096.547 4.142 1.588 5.945L.057 24l6.305-1.654a11.882 11.882 0 005.683 1.448h.005c6.554 0 11.89-5.335 11.893-11.893a11.821 11.821 0 00-3.48-8.413Z"/>
      </svg>
    `;
    whatsappBtn.onclick = function() {
      const whatsappUrl = `https://wa.me/${CONFIG.whatsappNumber}`;
      window.open(whatsappUrl, '_blank');
    };
    
    // Botón del Chatbot
    const chatbotBtn = document.createElement('button');
    chatbotBtn.className = 'chatbot-float-btn';
    chatbotBtn.setAttribute('aria-label', 'Abrir chatbot');
    chatbotBtn.innerHTML = `
      <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 12H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
      </svg>
    `;
    chatbotBtn.onclick = toggleChatbot;
    
    // Ventana del Chatbot
    const chatbotWindow = document.createElement('div');
    chatbotWindow.className = 'chatbot-window';
    chatbotWindow.id = 'chatbot-window';
    
    chatbotWindow.innerHTML = `
      <div class="chatbot-header">
        <h3>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" style="display: inline-block; vertical-align: middle;">
            <path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 12H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
          </svg>
          ASHAKids Bot
        </h3>
        <button class="chatbot-close-btn" aria-label="Cerrar chatbot">×</button>
      </div>
      <div class="chatbot-messages" id="chatbot-messages">
        <!-- Los mensajes se agregarán aquí dinámicamente -->
      </div>
    `;
    
    // Agregar elementos al contenedor
    floatContainer.appendChild(whatsappBtn);
    floatContainer.appendChild(chatbotBtn);
    floatContainer.appendChild(chatbotWindow);
    
    // Agregar al body
    document.body.appendChild(floatContainer);
    
    // Mostrar mensaje inicial
    showInitialMessage();
  }

  /**
   * Muestra el mensaje inicial del chatbot
   */
  function showInitialMessage() {
    const messagesContainer = document.getElementById('chatbot-messages');
    if (!messagesContainer) return;
    
    const roleConfig = CONFIG.roles[currentRole];
    if (!roleConfig) return;
    
    messagesContainer.innerHTML = `
      <div class="chatbot-message">
        <div class="chatbot-message-avatar">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 12H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
          </svg>
        </div>
        <div class="chatbot-message-content">
          <p>Hola 👋, soy el asistente de ASHAKids. ¿Qué deseas hacer hoy?</p>
          <div class="chatbot-options" id="chatbot-options">
            ${roleConfig.questions.map(question => 
              `<button class="chatbot-option-btn" data-question="${question}">${question}</button>`
            ).join('')}
          </div>
        </div>
      </div>
    `;
    
    // Agregar event listeners a los botones de opciones
    attachOptionListeners();
  }

  /**
   * Agrega event listeners a los botones de opciones
   */
  function attachOptionListeners() {
    const optionButtons = document.querySelectorAll('.chatbot-option-btn');
    optionButtons.forEach(btn => {
      btn.addEventListener('click', function() {
        const question = this.getAttribute('data-question');
        handleQuestionClick(question, this);
      });
    });
  }

  /**
   * Maneja el clic en una pregunta
   */
  function handleQuestionClick(question, buttonElement) {
    const messagesContainer = document.getElementById('chatbot-messages');
    if (!messagesContainer) return;
    
    const roleConfig = CONFIG.roles[currentRole];
    if (!roleConfig) return;
    
    // Deshabilitar el botón
    buttonElement.disabled = true;
    buttonElement.style.opacity = '0.6';
    
    // Obtener respuesta
    const response = roleConfig.responses[question] || CONFIG.defaultResponse;
    
    // Mostrar respuesta después de un pequeño delay
    setTimeout(() => {
      const responseHTML = `
        <div class="chatbot-message">
          <div class="chatbot-message-avatar">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 12H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
            </svg>
          </div>
          <div class="chatbot-message-content">
            <div class="chatbot-response">
              <p>${response}</p>
            </div>
            <div class="chatbot-options" style="margin-top: 15px;">
              ${roleConfig.questions.map(q => 
                `<button class="chatbot-option-btn" data-question="${q}">${q}</button>`
              ).join('')}
            </div>
          </div>
        </div>
      `;
      
      messagesContainer.insertAdjacentHTML('beforeend', responseHTML);
      
      // Scroll al final
      messagesContainer.scrollTop = messagesContainer.scrollHeight;
      
      // Re-attach listeners a las nuevas opciones
      attachOptionListeners();
    }, 500);
  }

  /**
   * Agrega event listeners generales
   */
  function attachEventListeners() {
    // Listener para cerrar el chatbot
    document.addEventListener('click', function(e) {
      if (e.target.classList.contains('chatbot-close-btn')) {
        toggleChatbot();
      }
    });
    
    // Cerrar al hacer clic fuera (opcional)
    document.addEventListener('click', function(e) {
      const chatbotWindow = document.getElementById('chatbot-window');
      const chatbotBtn = document.querySelector('.chatbot-float-btn');
      
      if (isOpen && 
          chatbotWindow && 
          !chatbotWindow.contains(e.target) && 
          !chatbotBtn.contains(e.target)) {
        // Opcional: descomentar para cerrar al hacer clic fuera
        // toggleChatbot();
      }
    });
  }

  /**
   * Abre o cierra el chatbot
   */
  function toggleChatbot() {
    const chatbotWindow = document.getElementById('chatbot-window');
    if (!chatbotWindow) return;
    
    isOpen = !isOpen;
    
    if (isOpen) {
      chatbotWindow.classList.add('active');
      // Scroll al inicio al abrir
      setTimeout(() => {
        const messagesContainer = document.getElementById('chatbot-messages');
        if (messagesContainer) {
          messagesContainer.scrollTop = 0;
        }
      }, 300);
    } else {
      chatbotWindow.classList.remove('active');
    }
  }

  // Inicializar cuando el DOM esté listo
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    // El DOM ya está listo, inicializar inmediatamente
    setTimeout(init, 100);
  }
  
})();

