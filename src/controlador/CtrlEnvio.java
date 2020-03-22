/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.Alumno;
import vista.EnvioCorreo;
import modelo.CorreoElectronico;

/**
 *
 * @author Lauti
 */
public class CtrlEnvio implements ActionListener {

    EnvioCorreo vEnvioCorreo;
    Alumno alumno;

    String mensaje;
    String asunto;
    
    /**
     * Crea y muestra la ventana modificar alumno
     * @param vEnCorreo Ventana para enviar correos en la Vista
     * @param alumno alumno receptor del mail
     */
    public CtrlEnvio(EnvioCorreo vEnCorreo, Alumno alumno) {
        this.alumno = alumno;
        this.vEnvioCorreo = vEnCorreo;

        this.vEnvioCorreo.btnEnviar.addActionListener(this);
        this.vEnvioCorreo.btnCancelar.addActionListener(this);
        
        rellenarMensaje(alumno);
        mostrarVentana();
    }

    public void mostrarVentana() {
        this.vEnvioCorreo.setVisible(true);
    }

    // Rellena campos del mensaje
    public boolean rellenarMensaje(Alumno alum) {
        this.mensaje = "Hola, " + alum.getNombre() + " " + alum.getApellido() + "\n"
                + "Este es un mensaje de prueba y tu DNI es: " + alum.getDni();
        this.asunto = "Sin asunto(Default)";

        this.vEnvioCorreo.cajaMensaje.setText(mensaje);
        this.vEnvioCorreo.cajaAsunto.setText(asunto);
        return true;
    }
    
    
    public void cargarDatosDelRegistroSeleccionado(Alumno alum) {
        this.alumno.setNombre(alum.getNombre());
        this.alumno.setApellido(alum.getApellido());
        this.alumno.setDni(alum.getDni());
        this.alumno.setGmail(alum.getGmail());
        rellenarMensaje(alumno);
    }

    public void limpiarFields() {
        this.vEnvioCorreo.cajaAsunto.setText(null);
        this.vEnvioCorreo.cajaMensaje.setText(null);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vEnvioCorreo.btnEnviar) {
            CorreoElectronico correo = new CorreoElectronico(this.alumno.getGmail(), this.asunto, this.mensaje);
            correo.enviarMensaje();
        }

        if (e.getSource() == this.vEnvioCorreo.btnEnviar) {
            try {
                try {
                    CorreoElectronico correo = new CorreoElectronico(alumno.getGmail(), asunto, mensaje);
                    correo.enviarMensaje();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al enviar un gmail a" + alumno.getGmail() + "\n" + "Verifique que el Gmail sea correcto" + "\nTipo de error" + ex, "Error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(null, "Todo bien");
            } catch (Exception ex2) {
                JOptionPane.showMessageDialog(null, "Error al enviar un gmail a" + ex2, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == this.vEnvioCorreo.btnCancelar) {
            this.vEnvioCorreo.setVisible(false);
            // Cancelar acción
        }
    }
}
