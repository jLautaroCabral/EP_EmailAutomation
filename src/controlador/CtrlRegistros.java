/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.mysql.jdbc.Connection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.Alumno;
import vista.*;
import modelo.Conexion;
import modelo.Consultas;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Lauti
 */
public class CtrlRegistros implements ActionListener {

    // Se inicializan y declaran objetos de Vista y Controlador
    
    Registros ventanaRegistros = null;
    RegistrarAlumno ventanaRegistrarAlumno = new RegistrarAlumno();
    ModificarAlumno ventanaModificarAlumno = new ModificarAlumno();
    EnvioCorreo ventanaEnviarCorreo = new EnvioCorreo();

    CtrlRegistrarAlumno ctrlRegisAlumno;
    CtrlModifAlum ctrlModifAlumn;
    CtrlEnvio ctrlEnvioCorreoMasivo;
    
    DefaultTableModel tableModel = new DefaultTableModel();

    /**
     * Crea y muestra la ventana registros
     * @param vRegistros Ventana de registros en la Vista
     */
    public CtrlRegistros(Registros vRegistros) {

        this.ventanaRegistros = vRegistros;

        this.ventanaRegistros.btnRegistrar.addActionListener(this);
        this.ventanaRegistros.btnModificar.addActionListener(this);
        this.ventanaRegistros.btnEliminar.addActionListener(this);
        this.ventanaRegistros.btnImprimirRegistro.addActionListener(this);
        this.ventanaRegistros.btnCorreoMasivo.addActionListener(this);
        this.ventanaRegistros.btnActualizar.addActionListener(this);

        iniciar();
    }

    // Configura y rellena la tabla con los datos de la base de datos
    private void iniciar() {

        this.ventanaRegistros.jTable.setModel(tableModel);

        tableModel.addColumn("Nombre");
        tableModel.addColumn("Apellido");
        tableModel.addColumn("DNI");
        tableModel.addColumn("Gmail");

        int[] anchos = {60, 60, 60, 100};
        for (int i = 0; i < this.ventanaRegistros.jTable.getColumnCount(); i++) {
            this.ventanaRegistros.jTable.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        this.ventanaRegistros.setTitle("Registro de alumnos");
        this.ventanaRegistros.setLocationRelativeTo(null);
        this.ventanaRegistros.setVisible(true);

        this.ventanaRegistrarAlumno.setTitle("Registrar un nuevo alumno");
        this.ventanaRegistrarAlumno.setLocationRelativeTo(null);

        this.ventanaModificarAlumno.setTitle("Modificar alumno");
        this.ventanaModificarAlumno.setLocationRelativeTo(null);

        this.ventanaEnviarCorreo.setTitle("Enviar gmail");
        this.ventanaEnviarCorreo.setLocationRelativeTo(null);

        actualizarTabla();
    }
    
    

    void actualizarTabla() {

        int count = this.ventanaRegistros.jTable.getRowCount();
        for (int i = count - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        try {
            Connection con = Conexion.getConnection();
            PreparedStatement ps;
            ResultSet rs;
            ResultSetMetaData rsMd;

            ps = con.prepareStatement("SELECT Nombre, Apellido, Dni, Gmail FROM alumnos");
            rs = ps.executeQuery();
            rsMd = rs.getMetaData();

            int cantidadColumnas = rsMd.getColumnCount();

            while (rs.next()) {
                Object[] registros = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    registros[i] = rs.getObject(i + 1);
                }
                tableModel.addRow(registros);
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e, "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    
    

    @Override
    public void actionPerformed(ActionEvent e) {

        // Al dar click en el boton Registrar crea un Controlador de registro de alumnos
        if (e.getSource() == ventanaRegistros.btnRegistrar) {
            if (ctrlRegisAlumno == null) {
                ctrlRegisAlumno = new CtrlRegistrarAlumno(ventanaRegistrarAlumno);
            } else {
                ctrlRegisAlumno.mostrarVentana();
            }
        }
        
        // Al dar click en el boton Modificar crea un Controlador de modificacion de alumnos
        if (e.getSource() == ventanaRegistros.btnModificar) {
            boolean camposCompletos = false;
            Alumno alum = new Alumno();
            try {
                alum.setNombre((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 0));
                alum.setApellido((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 1));
                alum.setDni((int) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 2));
                alum.setGmail((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 3));
                camposCompletos = true;

                if (ctrlModifAlumn == null && camposCompletos) {
                    ctrlModifAlumn = new CtrlModifAlum(ventanaModificarAlumno, String.valueOf(alum.getDni()));
                    ctrlModifAlumn.cargarValoresDelRegistro(alum);
                } else {
                    ctrlModifAlumn.cargarValoresDelRegistro(alum);
                    ctrlModifAlumn.mostrarVentana();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Asegurese de que seleccionó un registro\nTipo de error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Al dar click en el boton Eliminar elimina el alumno seleccionado
        if (e.getSource() == ventanaRegistros.btnEliminar) {
            try {
                Consultas consulta = new Consultas();
                Alumno alum = new Alumno();

                int dni = (int) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 2);
                int row = this.ventanaRegistros.jTable.getSelectedRow();

                alum.setDni(dni);
                if (consulta.eliminarAlumno(alum)) {
                    tableModel.removeRow(row);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Asegurese de que seleccinó un registro\nTipo de error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            }

        }

        // Al darle al boton Imprimir registro, crea un JasperReport a partir del alumno seleccionado
        if (e.getSource() == ventanaRegistros.btnImprimirRegistro) {
            try {
                Alumno alum = new Alumno();
                alum.setNombre((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 0));
                alum.setApellido((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 1));
                alum.setDni((int) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 2));
                alum.setGmail((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 3));
                
                JasperReport reporte = null;
                String ruta = "..\\src\\modelo\\reportTest1.jasper";
                File file = new File(ruta);
                Connection con = Conexion.getConnection();
                
                Map parametros = new HashMap();
                parametros.put("nombreAlumno", alum.getNombre());
                parametros.put("gmail", alum.getGmail());
                parametros.put("apellidoAlumno", alum.getApellido());
                
                reporte = (JasperReport) JRLoader.loadObject(file);
                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, new JREmptyDataSource());
                JasperViewer jasperView = new JasperViewer(jprint, false);
                jasperView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                jasperView.setVisible(true);
                        
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Tipo de error" + ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }

        // Al darle al boton enviar mail, se crea el controlador
        if (e.getSource() == ventanaRegistros.btnCorreoMasivo) {
            try {
                Alumno alum = new Alumno();
                
                alum.setNombre((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 0));
                alum.setApellido((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 1));
                alum.setDni((int) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 2));
                alum.setGmail((String) this.ventanaRegistros.jTable.getValueAt(this.ventanaRegistros.jTable.getSelectedRow(), 3));

                if (ctrlEnvioCorreoMasivo == null) {
                    ctrlEnvioCorreoMasivo = new CtrlEnvio(ventanaEnviarCorreo, alum);
                    ctrlEnvioCorreoMasivo.rellenarMensaje(alum);
                } else {
                    ctrlEnvioCorreoMasivo.rellenarMensaje(alum);
                    ctrlEnvioCorreoMasivo.mostrarVentana();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Tipo de error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
        
        if (e.getSource() == ventanaRegistros.btnActualizar) {
            actualizarTabla();
        }
    }
}
