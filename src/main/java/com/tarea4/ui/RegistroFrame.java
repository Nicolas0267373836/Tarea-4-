package com.tarea4.ui;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import java.awt.Insets;

public class RegistroFrame extends JFrame {

    private final UsuarioDAO dao;
    private final JTextField usuario = new JTextField();
    private final JTextField nombre = new JTextField();
    private final JTextField apellido = new JTextField();
    private final JTextField telefono = new JTextField();
    private final JTextField correo = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JPasswordField confirmar = new JPasswordField();

    public RegistroFrame(UsuarioDAO dao) {
        this.dao = dao;
        setTitle("Registro de usuario");
        setSize(460, 550);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(0, 12));
        principal.setBorder(BorderFactory.createEmptyBorder(18, 52, 25, 52));
        principal.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("REGISTRO", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        principal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(2, 0, 2, 0);
        agregarCampo(formulario, c, 0, "Nombre de Usuario", usuario);
        agregarCampo(formulario, c, 2, "Nombre", nombre);
        agregarCampo(formulario, c, 4, "Apellido", apellido);
        agregarCampo(formulario, c, 6, "Número de Teléfono", telefono);
        agregarCampo(formulario, c, 8, "Correo Electrónico", correo);
        agregarCampo(formulario, c, 10, "Contraseña", password);
        agregarCampo(formulario, c, 12, "Confirmar Contraseña", confirmar);
        principal.add(formulario, BorderLayout.CENTER);

        JButton guardar = new JButton("Registrar");
        guardar.addActionListener(event -> registrar());
        principal.add(guardar, BorderLayout.SOUTH);
        add(principal);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints c, int fila, String texto, JTextField campo) {
        c.gridy = fila;
        panel.add(new JLabel(texto + ":"), c);
        c.gridy = fila + 1;
        campo.setPreferredSize(new Dimension(290, 25));
        panel.add(campo, c);
    }

    private void registrar() {
        if (usuario.getText().trim().isEmpty()) { mensaje("nombre de usuario"); return; }
        if (nombre.getText().trim().isEmpty()) { mensaje("nombre"); return; }
        if (apellido.getText().trim().isEmpty()) { mensaje("apellido"); return; }
        if (telefono.getText().trim().isEmpty()) { mensaje("número de teléfono"); return; }
        if (correo.getText().trim().isEmpty()) { mensaje("correo electrónico"); return; }

        String clave = new String(password.getPassword());
        String claveConfirmada = new String(confirmar.getPassword());
        if (clave.isEmpty()) { mensaje("contraseña"); return; }
        if (claveConfirmada.isEmpty()) { mensaje("confirmar contraseña"); return; }
        if (!clave.equals(claveConfirmada)) {
            JOptionPane.showMessageDialog(this, "La contraseña y la confirmación no coinciden.");
            return;
        }

        Usuario nuevo = new Usuario();
        nuevo.setUsuario(usuario.getText().trim());
        nuevo.setNombre(nombre.getText().trim());
        nuevo.setApellido(apellido.getText().trim());
        nuevo.setTelefono(telefono.getText().trim());
        nuevo.setCorreo(correo.getText().trim());

        try {
            dao.registrar(nuevo, clave);
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            dispose();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar. El usuario o correo puede estar repetido.");
        }
    }

    private void mensaje(String campo) {
        JOptionPane.showMessageDialog(this, "El campo " + campo + " es obligatorio.");
    }
}
