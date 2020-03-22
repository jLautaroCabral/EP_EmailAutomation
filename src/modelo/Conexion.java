/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.mysql.jdbc.Connection;
import java.awt.HeadlessException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Lauti
 */

// Conexion a la base de datos
public class Conexion {
    private static final  String URL = "jdbc:mysql://localhost:3306/pruebas";
    private static final  String USERNAME = "root";
    private static final  String PASSWORD = "1234";
    
    
    public static Connection getConnection() {
        
        Connection con = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con =(Connection) DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch(HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en TestConecction: " + e);
        }
        
        return con;
        
    }
}
