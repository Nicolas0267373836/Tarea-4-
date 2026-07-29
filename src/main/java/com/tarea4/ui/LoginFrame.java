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
import java.awt.GridLayout;
import java.awt.Insets;

public class LoginFrame extends JFrame {

    private final UsuarioDAO dao;
    private final JTextField usuarioField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginFrame(UsuarioDAO dao) {
        this.dao = dao;
        setTitle("Login de usuarios");
        setSize(430, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(0, 15));
        principal.setBorder(BorderFactory.createEmptyBorder(18, 42, 25, 42));
        principal.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("LOGIN", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        principal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(4, 0, 4, 0);

        agregarCampo(formulario, c, 0, "Nombre de Usuario", usuarioField);
        agregarCampo(formulario, c, 2, "Contraseña", passwordField);
        principal.add(formulario, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 0));
        botones.setBackground(Color.WHITE);
        JButton entrar = new JButton("Entrar");
        JButton registrar = new JButton("Registrarse");
        entrar.addActionListener(event -> entrar());
        registrar.addActionListener(event -> new RegistroFrame(dao).setVisible(true));
        botones.add(entrar);
        botones.add(registrar);
        principal.add(botones, BorderLayout.SOUTH);

        add(principal);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints c, int fila, String texto, JTextField campo) {
        c.gridy = fila;
        panel.add(new JLabel(texto + ":"), c);
        c.gridy = fila + 1;
        campo.setPreferredSize(new Dimension(250, 28));
        panel.add(campo, c);
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
