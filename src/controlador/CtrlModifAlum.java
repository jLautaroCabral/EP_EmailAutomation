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
import vista.ModificarAlumno;
import modelo.Consultas;

/**
 *
 * @author Lauti
 */
public class CtrlModifAlum implements ActionListener {
    
    ModificarAlumno vModificarAlumno;
    String dniAlumnoAModificar;
    Alumno referenciaAlum;
    
    /**
     * Crea y muestra la ventana modificar alumno
     * @param vModifAlumn Ventana para registrar alumnos en la Vista
     * @param dni Dni del alumno a modificar
     */
    public CtrlModifAlum(ModificarAlumno vModifAlumn, String dni) {
        this.dniAlumnoAModificar = dni;
        
        this.vModificarAlumno = vModifAlumn;
        mostrarVentana();
        
        this.vModificarAlumno.btnModificar.addActionListener(this);
        this.vModificarAlumno.btnCancelar.addActionListener(this);    
    }
    
    // Modifica el alumno en la base de datos segun los campos
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.vModificarAlumno.btnModificar) {
            Alumno alumno = new Alumno();
            Consultas consulta = new Consultas();
            try {
            alumno.setNombre(this.vModificarAlumno.nombre.getText());
            alumno.setApellido(this.vModificarAlumno.apellido.getText());
            alumno.setDni(Integer.parseInt(this.vModificarAlumno.dni.getText()));
            alumno.setGmail(this.vModificarAlumno.gmail.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Asegurese que todos los campos estén completos y el campo DNI sea un valor númerico\nTipo de error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            consulta.modificarAlumno(alumno, dniAlumnoAModificar);
            
            limpiarFields();
            this.vModificarAlumno.setVisible(false);
        }
        
        if(e.getSource() == this.vModificarAlumno.btnCancelar) {
            limpiarFields();
            this.vModificarAlumno.setVisible(false);
        }
    }
    
    void limpiarFields() {
        this.vModificarAlumno.nombre.setText(null);
        this.vModificarAlumno.apellido.setText(null);
        this.vModificarAlumno.dni.setText(null);
        this.vModificarAlumno.gmail.setText(null);
    }
    
    public void mostrarVentana() {
        this.vModificarAlumno.setVisible(true);
    }
    
    // Cargo los datos del alumno a modificar en los campos
    void cargarValoresDelRegistro(Alumno a) {
        this.vModificarAlumno.nombre.setText(a.getNombre());
        this.vModificarAlumno.apellido.setText(a.getApellido());
        this.vModificarAlumno.dni.setText(String.valueOf(a.getDni()));
        this.vModificarAlumno.gmail.setText(a.getGmail());
    }
    
}
