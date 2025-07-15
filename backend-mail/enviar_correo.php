<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

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
    $mail->Subject = 'Confirmacion de Cita - ASHAKids';
    $mail->Body = "
    <div style='font-family: Arial, sans-serif; background-color: #fffbe6; padding: 20px; border-radius: 10px; color: #333;'>
        <h2 style='color: #ff9900;'>🎉 ¡Cita confirmada en ASHAKids!</h2>
        <p>Hola <strong>$nombreTutor</strong>,</p>
        <p>Te confirmamos que tu cita ha sido agendada con éxito.</p>
        <table style='width: 100%; margin-top: 20px; border-collapse: collapse;'>
            <tr>
                <td style='padding: 8px;'><strong>👶 Nombre del niño(a):</strong></td>
                <td style='padding: 8px;'>$nombreNiño</td>
            </tr>
            <tr>
                <td style='padding: 8px;'><strong>🧑‍⚕️ Terapeuta:</strong></td>
                <td style='padding: 8px;'>$terapeuta</td>
            </tr>
            <tr>
                <td style='padding: 8px;'><strong>📅 Fecha:</strong></td>
                <td style='padding: 8px;'>$fecha</td>
            </tr>
            <tr>
                <td style='padding: 8px;'><strong>⏰ Hora:</strong></td>
                <td style='padding: 8px;'>$hora</td>
            </tr>
            <tr>
                <td style='padding: 8px;'><strong>🎯 Tema:</strong></td>
                <td style='padding: 8px;'>$tema</td>
            </tr>
            <tr>
                <td style='padding: 8px;'><strong>⌛ Duración:</strong></td>
                <td style='padding: 8px;'>$duracion minutos</td>
            </tr>
        </table>
        <p style='margin-top: 20px;'>🔗 En breve recibirás un enlace de Zoom para acceder a tu sesión.</p>
        <p style='margin-top: 20px;'>Gracias por confiar en <strong>ASHAKids</strong>.</p>
        <p style='font-size: 14px; color: #888;'>Este mensaje fue generado automáticamente. No respondas a este correo.</p>
    </div>
";

    $mail->send();
    echo '✅ Cita registrada y correo enviado correctamente.';
} catch (Exception $e) {
    echo "❌ Error al enviar el correo: {$mail->ErrorInfo}";
}
