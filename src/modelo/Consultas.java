/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import modelo.Conexion;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Lauti
 */
public class Consultas {

    /**
     * 
     * @param alumno Alumno que registrar
     * @return Devuelve true si la operacion fue un exito
     */
    public boolean registrarAlumno(Alumno alumno) {
        try {
            Connection con = Conexion.getConnection();
            PreparedStatement ps;
            ResultSet rs;

            ps = con.prepareStatement("INSERT INTO alumnos (Nombre, Apellido, Dni, Gmail) VALUES (?, ?, ?, ?)");
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());
            ps.setString(3, String.valueOf(alumno.getDni()));
            ps.setString(4, alumno.getGmail());

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Se ha agregado el registro", null, JOptionPane.PLAIN_MESSAGE);
            }

            con.close();
            return true;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la consulta SQL: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception ex2) {
            JOptionPane.showMessageDialog(null, "" + ex2, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * 
     * @param alumno Alumno que eliminar
     * @return Devuelve true si la operacion fue un exito
     */
    public boolean eliminarAlumno(Alumno alumno) {
        try {
            Connection con = Conexion.getConnection();
            PreparedStatement ps;
            ResultSet rs;

            ps = con.prepareStatement("DELETE FROM alumnos WHERE Dni = ?");
            ps.setString(1, String.valueOf(alumno.getDni()));
            

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Se ha eliminado el registro", null, JOptionPane.PLAIN_MESSAGE);
            }

            con.close();
            return true;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la consulta SQL: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception ex2) {
            JOptionPane.showMessageDialog(null, "" + ex2, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * 
     * @param alumno Alumno que modificar en la base de datos
     * @return Devuelve true si la operacion fue un exito
     */
    public boolean modificarAlumno(Alumno alumno, String dniAlumnoAModif) {
        try {
            Connection con = Conexion.getConnection();
            PreparedStatement ps;
            ResultSet rs;

            ps = con.prepareStatement("UPDATE alumnos SET Nombre = ?, Apellido = ?, Dni = ?, Gmail = ? WHERE Dni = ?");
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());
            ps.setString(3, String.valueOf(alumno.getDni()));
            ps.setString(4, alumno.getGmail());
            ps.setString(5, dniAlumnoAModif);
            

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Se ha modificado el registro", null, JOptionPane.PLAIN_MESSAGE);
            }

            con.close();
            return true;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la consulta SQL: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception ex2) {
            JOptionPane.showMessageDialog(null, "" + ex2, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

}
