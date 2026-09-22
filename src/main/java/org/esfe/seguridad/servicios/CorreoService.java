package org.esfe.seguridad.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    @Value("${app.recuperacion.url}")
    private String urlRecuperacion;

    public void enviarEnlaceRecuperacion(String correo, String token) {
        String enlace = urlRecuperacion + token;
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(correo);
        mensaje.setSubject("MarketLocalShirts - Restablecer contrasena");
        mensaje.setText(
                "Recibimos una solicitud para restablecer tu contrasena.\n\n"
                        + "El enlace es valido por 30 minutos:\n"
                        + enlace
                        + "\n\nSi no fuiste tu, ignora este correo."
        );
        try {
            mailSender.send(mensaje);
        } catch (Exception ex) {
            throw new IllegalArgumentException("No se pudo enviar el correo. Revise usuario y contrasena de aplicacion de Gmail");
        }
    }
}
