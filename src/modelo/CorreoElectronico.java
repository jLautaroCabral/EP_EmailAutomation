/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import modelo.CorreoRemitente;

/**
 *
 * @author Lauti
 */
public class CorreoElectronico {

    String recep;
    String asunt;
    String mensaj;
    String remitente = CorreoRemitente.REMITENTE;    // Aquí va el Gmail del remitente
    String passRemitente = CorreoRemitente.CONTRASEÑA;    //Aquí va la contraseña del Gmail del remitente
    
    /**
     * Crea el envio de un correo electronico
     * @param receptor
     * @param asunto
     * @param mensaje 
     */
    public CorreoElectronico(String receptor, String asunto, String mensaje) {
        this.recep = receptor;
        this.asunt = asunto;
        this.mensaj = mensaje;
    }
    
    
    public void enviarMensaje() {
        try {
            
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recep));
            message.setSubject(asunt);
            message.setText(mensaj);

            Transport t = session.getTransport("smtp");
            t.connect(remitente, passRemitente);
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            
            System.out.println("Mensaje de " + remitente + " enviado a " + recep);
            
            t.close();
        } catch (MessagingException ex) {
            JOptionPane.showMessageDialog(null, "Error al enviar correo\nTipo de error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
