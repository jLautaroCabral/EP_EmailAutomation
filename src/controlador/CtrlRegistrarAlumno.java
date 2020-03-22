/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.RegistrarAlumno;
import modelo.*;

/**
 *
 * @author Lauti
 */
public class CtrlRegistrarAlumno implements ActionListener {

    RegistrarAlumno vRegistrarAlumno;
    /**
     * Crea y muestra la ventana registrar alumno
     * @param vRegistrarAlum Ventana para registrar alumnos en la Vista
     */
    public CtrlRegistrarAlumno(RegistrarAlumno vRegistrarAlum) {

        this.vRegistrarAlumno = vRegistrarAlum;
        vRegistrarAlum.setVisible(true);

        this.vRegistrarAlumno.btnAgregar.addActionListener(this);
        this.vRegistrarAlumno.btnCancelar.addActionListener(this);
    }

    // Agrega el alumno a la base de datos segun los campos
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vRegistrarAlumno.btnAgregar) {
            Alumno alumno = new Alumno();
            Consultas consulta = new Consultas();
            try {
                alumno.setNombre(this.vRegistrarAlumno.nombre.getText());
                alumno.setApellido(this.vRegistrarAlumno.apellido.getText());
                alumno.setDni(Integer.parseInt(this.vRegistrarAlumno.dni.getText()));
                alumno.setGmail(this.vRegistrarAlumno.gmail.getText());
            } catch (NumberFormatException ex2) {
                JOptionPane.showMessageDialog(null, "Asegurese de que completó todos los campos y que el DNI sólo tiene valores númericos\nTipo de error: " + ex2, "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            consulta.registrarAlumno(alumno);
            
            limpiarFields();
            this.vRegistrarAlumno.setVisible(false);
        }
        
        // Cancela la accion ocultando la ventana y limpiando los campos
        if (e.getSource() == this.vRegistrarAlumno.btnCancelar) {
            limpiarFields();
            this.vRegistrarAlumno.setVisible(false);
        }
    }

    void limpiarFields() {
        this.vRegistrarAlumno.nombre.setText(null);
        this.vRegistrarAlumno.apellido.setText(null);
        this.vRegistrarAlumno.dni.setText(null);
        this.vRegistrarAlumno.gmail.setText(null);
    }

    public void mostrarVentana() {
        this.vRegistrarAlumno.setVisible(true);
    }

}
