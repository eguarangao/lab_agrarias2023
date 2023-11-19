package Controller;

import DAO.UsuarioDAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.primefaces.PrimeFaces;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

@RequestScoped
@Named
public class mailBean {
    private static final SecureRandom secureRandom = new SecureRandom();

    private String email;

    // Getter y Setter para el correo electrónico
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Método para enviar el correo de recuperación de contraseña
    public void recoverPassword() {
        // Aquí se debe implementar la lógica para recuperar la contraseña
        // y enviar el correo de restablecimiento

        // Se crea una nueva contraseña (aquí puedes usar tu lógica para generar una nueva contraseña)
        String nuevaContrasena = generarNuevaContrasena();

        // Lógica para enviar el correo
        enviarCorreoRecuperacion(nuevaContrasena);

        // Mensaje de éxito
        PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
    }

    // Método para enviar el correo de recuperación de contraseña
    private void enviarCorreoRecuperacion(String nuevaContrasena) {
        // Configuración de los detalles del servidor de correo
        final String username = "josemadero2023enero@gmail.com"; // Reemplaza con tu dirección de correo
        final String password = "wglbmbjbibwpersi"; // Reemplaza con tu contraseña
        String destinatario = getEmail();
        String asunto = "Recuperación de Contraseña";
        String mensaje = "Tu nueva contraseña es: " + nuevaContrasena;

        // Propiedades para la conexión con el servidor SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Autenticación con el servidor de correo
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Creación del mensaje
            Message correo = new MimeMessage(session);
            correo.setFrom(new InternetAddress(username));
            correo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            correo.setSubject(asunto);
            correo.setText(mensaje);

            // Envío del mensaje
            Transport.send(correo);
            UsuarioDAO dao = new UsuarioDAO();
            dao.resetPassword(destinatario,nuevaContrasena);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para generar una nueva contraseña (debes implementar tu propia lógica aquí)
    private String generarNuevaContrasena() {
        // Implementa aquí la lógica para generar una nueva contraseña
        byte[] bytes = new byte[20]; // Longitud del token
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
