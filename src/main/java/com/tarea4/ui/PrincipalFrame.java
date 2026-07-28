package com.tarea4.ui;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.model.Usuario;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

public class PrincipalFrame extends JFrame {

    private UsuarioDAO dao;
    private DefaultTableModel model = new DefaultTableModel(
            new String[]{"Nombre", "Apellido", "Teléfono", "Correo electrónico", "Usuario"}, 0);
    private JTable tabla = new JTable(model);
    private List<Usuario> usuarios = new ArrayList<>();

    public PrincipalFrame(UsuarioDAO dao) {
        this.dao = dao;
        setTitle("Clientes registrados");
        setSize(820, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        JButton nuevo = new JButton("Nuevo");
        JButton actualizar = new JButton("Actualizar");
        JButton eliminar = new JButton("Eliminar");
        JButton cerrar = new JButton("Cerrar sesión");

        nuevo.addActionListener(event -> abrirFormulario(null));
        actualizar.addActionListener(event -> actualizar());
        eliminar.addActionListener(event -> eliminar());
        cerrar.addActionListener(event -> cerrarSesion());

        botones.add(nuevo);
        botones.add(actualizar);
        botones.add(eliminar);
        botones.add(cerrar);
        add(botones, BorderLayout.SOUTH);
        cargarTabla();
    }

    public void cargarTabla() {
        try {
            usuarios = dao.listarUsuarios();
            model.setRowCount(0);
            for (Usuario usuario : usuarios) {
                model.addRow(new Object[]{usuario.getNombre(), usuario.getApellido(), usuario.getTelefono(), usuario.getCorreo(), usuario.getUsuario()});
            }
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la lista: " + error.getMessage());
        }
    }

    private Usuario seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.");
            return null;
        }
        return usuarios.get(fila);
    }

    private void actualizar() {
        Usuario usuario = seleccionado();
        if (usuario != null) abrirFormulario(usuario);
    }

    private void eliminar() {
        Usuario usuario = seleccionado();
        if (usuario == null) return;

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar al usuario " + usuario.getUsuario() + "?",
                "Eliminar usuario", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(usuario.getId());
                cargarTabla();
            } catch (Exception error) {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.");
            }
        }
    }

    private void abrirFormulario(Usuario usuario) {
        new UsuarioDialog(this, dao, usuario).setVisible(true);
    }

    private void cerrarSesion() {
        dispose();
        new LoginFrame(dao).setVisible(true);
    }
}
