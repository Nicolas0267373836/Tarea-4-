package com.tarea4.ui;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class UsuarioDialog extends JDialog {

    private final PrincipalFrame principal;
    private final UsuarioDAO dao;
    private final Usuario usuario;
    private final JTextField usuarioField = new JTextField();
    private final JTextField nombre = new JTextField();
    private final JTextField apellido = new JTextField();
    private final JTextField telefono = new JTextField();
    private final JTextField correo = new JTextField();
    private final JPasswordField password = new JPasswordField();

    public UsuarioDialog(JFrame parent, UsuarioDAO dao, Usuario usuario) {
        super(parent, true);
        this.principal = (PrincipalFrame) parent;
        this.dao = dao;
        this.usuario = usuario;
        setTitle(usuario == null ? "Nuevo usuario" : "Actualizar usuario");
        setSize(470, 490);
        setLocationRelativeTo(parent);

        JPanel principalPanel = new JPanel(new BorderLayout(0, 12));
        principalPanel.setBorder(BorderFactory.createEmptyBorder(18, 52, 25, 52));
        principalPanel.setBackground(Color.WHITE);

        JLabel titulo = new JLabel(usuario == null ? "NUEVO USUARIO" : "ACTUALIZAR USUARIO", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        principalPanel.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(2, 0, 2, 0);
        agregar(formulario, c, 0, "Nombre de Usuario", usuarioField);
        agregar(formulario, c, 2, "Nombre", nombre);
        agregar(formulario, c, 4, "Apellido", apellido);
        agregar(formulario, c, 6, "Teléfono", telefono);
        agregar(formulario, c, 8, "Correo Electrónico", correo);
        agregar(formulario, c, 10,
                usuario == null ? "Contraseña" : "Nueva Contraseña (opcional)", password);
        principalPanel.add(formulario, BorderLayout.CENTER);

        if (usuario != null) {
            usuarioField.setText(usuario.getUsuario());
            nombre.setText(usuario.getNombre());
            apellido.setText(usuario.getApellido());
            telefono.setText(usuario.getTelefono());
            correo.setText(usuario.getCorreo());
        }

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 0));
        botones.setBackground(Color.WHITE);
        JButton cancelar = new JButton("Cancelar");
        JButton guardar = new JButton(usuario == null ? "Registrar" : "Guardar Cambios");
        cancelar.addActionListener(event -> dispose());
        guardar.addActionListener(event -> guardar());
        botones.add(cancelar);
        botones.add(guardar);
        principalPanel.add(botones, BorderLayout.SOUTH);
        add(principalPanel);
    }

    private void agregar(JPanel panel, GridBagConstraints c, int fila, String texto, JTextField campo) {
        c.gridy = fila;
        panel.add(new JLabel(texto + ":"), c);
        c.gridy = fila + 1;
        campo.setPreferredSize(new Dimension(300, 25));
        panel.add(campo, c);
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
