<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// Requiere PHPMailer
require 'PHPMailer/src/Exception.php';
require 'PHPMailer/src/PHPMailer.php';
require 'PHPMailer/src/SMTP.php';

// Recoger los datos del formulario
$nombreNiño   = $_POST['nombre_nino'];
$terapeuta    = $_POST['terapeuta'];
$fecha        = $_POST['fecha'];
$hora         = $_POST['hora'];
$duracion     = $_POST['duracion'];
$tema         = $_POST['tema'];
$nombreTutor  = $_POST['nombreTutor'];
$correoPadre  = $_POST['correo_padre'];

$mail = new PHPMailer(true);

try {
    // Configuración del servidor SMTP
    $mail->isSMTP();
    $mail->Host       = 'smtp.gmail.com';
    $mail->SMTPAuth   = true;
    $mail->Username   = 'sergiosk357@gmail.com';
    $mail->Password   = 'gzppeytlvqonvjor';
    $mail->SMTPSecure = 'tls';
    $mail->Port       = 587;

    // Cabeceras del correo
    $mail->setFrom('sergiosk357@gmail.com', 'ASHAKids');
    $mail->addAddress($correoPadre, $nombreTutor);

    // Contenido del correo
    $mail->isHTML(true);
    $mail->Subject = 'Boletin Informativo - ASHAKids';
$mail->Body = "
    <div style='font-family: Arial, sans-serif; background-color: #fef9f3; padding: 30px; border-radius: 12px; color: #333;'>
        <h1 style='color: #ff9900; text-align: center;'>📰 Boletín ASHAKids</h1>
        
        <p>Hola <strong>familia ASHAKids</strong>,</p>

        <p>Esperamos que tú y tu pequeño se encuentren muy bien. Aquí te compartimos las últimas novedades y recursos para acompañarte en el desarrollo comunicativo de tu niño(a).</p>

        <hr style='border: none; border-top: 2px dashed #ff9900; margin: 20px 0;'>

        <h3 style='color: #cc6600;'>🎯 Novedades del mes</h3>
        <ul>
            <li>🧩 Nuevo juego interactivo en el <strong>Rincón Divertido</strong>.</li>
            <li>📅 Disponibles nuevas fechas para sesiones con nuestros terapeutas especializados.</li>
            <li>📚 Artículo destacado: <em>“5 tips para estimular el lenguaje en casa”</em>.</li>
        </ul>

        <h3 style='color: #cc6600;'>📌 Recomendación de la semana</h3>
        <blockquote style='background-color: #fff6e0; padding: 15px; border-left: 5px solid #ffcc00; font-style: italic;'>
            “El juego es el lenguaje de la infancia. A través del juego, los niños expresan lo que las palabras no pueden decir.”
        </blockquote>

        <h3 style='color: #cc6600;'>🔗 Recursos útiles</h3>
        <ul>
            <li><a href='https://ashakids.pe/recursos' style='color: #ff9900;'>Guías prácticas para padres</a></li>
            <li><a href='https://ashakids.pe/juegos' style='color: #ff9900;'>Juegos para estimular el habla</a></li>
        </ul>

        <p style='margin-top: 30px;'>Gracias por ser parte de la familia ASHAKids 💛</p>

        <p style='font-size: 13px; color: #999; margin-top: 20px;'>
            Este boletín es informativo. Si deseas dejar de recibir estos correos, puedes <a href='#' style='color: #999;'>cancelar tu suscripción aquí</a>.
        </p>
    </div>
";


    $mail->send();
    echo '✅ Cita registrada y correo enviado correctamente.';
} catch (Exception $e) {
    echo "❌ Error al enviar el correo: {$mail->ErrorInfo}";
}
