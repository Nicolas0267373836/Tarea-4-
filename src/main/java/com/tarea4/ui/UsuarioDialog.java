package com.tarea4.ui;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.model.Usuario;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class UsuarioDialog extends JDialog {

    private PrincipalFrame principal;
    private UsuarioDAO dao;
    private Usuario usuario;
    private JTextField usuarioField = new JTextField();
    private JTextField nombre = new JTextField();
    private JTextField apellido = new JTextField();
    private JTextField telefono = new JTextField();
    private JTextField correo = new JTextField();
    private JPasswordField password = new JPasswordField();

    public UsuarioDialog(JFrame parent, UsuarioDAO dao, Usuario usuario) {
        super(parent, true);
        this.principal = (PrincipalFrame) parent;
        this.dao = dao;
        this.usuario = usuario;
        setTitle(usuario == null ? "Nuevo usuario" : "Actualizar usuario");
        setSize(400, 390);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(13, 1, 5, 5));
        agregar(panel, "Nombre de usuario:", usuarioField);
        agregar(panel, "Nombre:", nombre);
        agregar(panel, "Apellido:", apellido);
        agregar(panel, "Teléfono:", telefono);
        agregar(panel, "Correo:", correo);
        agregar(panel, usuario == null ? "Contraseña:" : "Nueva contraseña (opcional):", password);

        if (usuario != null) {
            usuarioField.setText(usuario.getUsuario());
            nombre.setText(usuario.getNombre());
            apellido.setText(usuario.getApellido());
            telefono.setText(usuario.getTelefono());
            correo.setText(usuario.getCorreo());
        }

        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(event -> guardar());
        panel.add(guardar);
        add(panel);
    }

    private void agregar(JPanel panel, String texto, JTextField campo) {
        panel.add(new JLabel(texto));
        panel.add(campo);
    }

    private void guardar() {
        if (usuarioField.getText().trim().isEmpty() || nombre.getText().trim().isEmpty()
                || apellido.getText().trim().isEmpty() || telefono.getText().trim().isEmpty()
                || correo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos de datos son obligatorios.");
            return;
        }

        String clave = new String(password.getPassword());
        if (usuario == null && clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña es obligatoria.");
            return;
        }

        Usuario datos = usuario == null ? new Usuario() : usuario;
        datos.setUsuario(usuarioField.getText().trim());
        datos.setNombre(nombre.getText().trim());
        datos.setApellido(apellido.getText().trim());
        datos.setTelefono(telefono.getText().trim());
        datos.setCorreo(correo.getText().trim());

        try {
            if (usuario == null) dao.registrar(datos, clave);
            else dao.actualizar(datos, clave);
            principal.cargarTabla();
            dispose();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar. Revise que usuario y correo no estén repetidos.");
        }
    }
}
