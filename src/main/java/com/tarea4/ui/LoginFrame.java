package com.tarea4.ui;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.model.Usuario;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class LoginFrame extends JFrame {

    private UsuarioDAO dao;
    private JTextField usuarioField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();

    public LoginFrame(UsuarioDAO dao) {
        this.dao = dao;
        setTitle("Login de usuarios");
        setSize(380, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.add(new JLabel("Nombre de usuario:"));
        panel.add(usuarioField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);

        JButton entrar = new JButton("Iniciar sesión");
        entrar.addActionListener(event -> entrar());
        panel.add(entrar);

        JButton registrar = new JButton("Registrarse");
        registrar.addActionListener(event -> new RegistroFrame(dao).setVisible(true));
        panel.add(registrar);

        add(panel);
    }

    private void entrar() {
        String usuario = usuarioField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar su usuario y contraseña, si no está registrado debe registrarse.");
            return;
        }

        try {
            Usuario usuarioEncontrado = dao.iniciarSesion(usuario, password);
            if (usuarioEncontrado == null) {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
                return;
            }
            dispose();
            new PrincipalFrame(dao).setVisible(true);
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "Error al conectar con MySQL: " + error.getMessage());
        }
    }
}
