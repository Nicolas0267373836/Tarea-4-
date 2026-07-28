package com.tarea4.ui;

import com.tarea4.model.Usuario;
import com.tarea4.service.UsuarioService;
import com.tarea4.session.SesionUsuario;
import com.tarea4.ui.components.Dialogs;
import com.tarea4.ui.components.RoundedPanel;
import com.tarea4.ui.theme.UITheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Ventana de inicio de sesión.
 */
public final class LoginFrame extends JFrame {

    private final UsuarioService usuarioService;
    private final JTextField usuarioField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton entrarButton = UITheme.botonPrimario("Iniciar sesión");

    public LoginFrame(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        setTitle("Sistema de Usuarios - Iniciar sesión");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(820, 540));
        setSize(960, 600);
        setLocationRelativeTo(null);
        setContentPane(crearContenido());

        getRootPane().setDefaultButton(entrarButton);
        entrarButton.addActionListener(this::iniciarSesion);
    }

    private JPanel crearContenido() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BACKGROUND);
        root.add(crearMarca(), BorderLayout.WEST);
        root.add(crearFormulario(), BorderLayout.CENTER);
        return root;
    }

    private JPanel crearMarca() {
        JPanel panel = new JPanel();
        panel.setBackground(UITheme.NAVY);
        panel.setPreferredSize(new Dimension(360, 600));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 42, 60, 42));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel insignia = new JLabel("TAREA 4");
        insignia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        insignia.setForeground(new java.awt.Color(147, 197, 253));
        insignia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("<html>Sistema de<br>Usuarios</html>");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(UITheme.WHITE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descripcion = new JLabel(
                "<html>Administra usuarios de forma<br>"
                        + "segura, sencilla y organizada.</html>"
        );
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descripcion.setForeground(new java.awt.Color(203, 213, 225));
        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        descripcion.setMaximumSize(new Dimension(270, 90));

        panel.add(insignia);
        panel.add(Box.createVerticalStrut(18));
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(24));
        panel.add(descripcion);
        panel.add(Box.createVerticalGlue());

        JLabel tecnologia = new JLabel("Java  •  Swing  •  MySQL  •  POO");
        tecnologia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tecnologia.setForeground(new java.awt.Color(148, 163, 184));
        tecnologia.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));
        tecnologia.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(tecnologia);

        return panel;
    }

    private JPanel crearFormulario() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UITheme.BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(36, 48, 36, 48));

        RoundedPanel card = new RoundedPanel(24, UITheme.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(38, 42, 38, 42));
        card.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(0, 0, 8, 0);

        JLabel titulo = new JLabel("Bienvenido");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titulo.setForeground(UITheme.TEXT);
        constraints.gridy = 0;
        card.add(titulo, constraints);

        JLabel subtitulo = new JLabel("Ingresa tus datos para continuar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(UITheme.MUTED);
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 26, 0);
        card.add(subtitulo, constraints);

        UITheme.prepararCampo(usuarioField);
        UITheme.prepararCampo(passwordField);
        passwordField.setEchoChar('●');

        constraints.insets = new Insets(0, 0, 7, 0);
        constraints.gridy = 2;
        card.add(etiqueta("Nombre de usuario"), constraints);
        constraints.gridy = 3;
        constraints.insets = new Insets(0, 0, 16, 0);
        card.add(usuarioField, constraints);

        constraints.gridy = 4;
        constraints.insets = new Insets(0, 0, 7, 0);
        card.add(etiqueta("Contraseña"), constraints);
        constraints.gridy = 5;
        constraints.insets = new Insets(0, 0, 8, 0);
        card.add(passwordField, constraints);

        JCheckBox mostrarPassword = new JCheckBox("Mostrar contraseña");
        mostrarPassword.setBackground(UITheme.WHITE);
        mostrarPassword.setForeground(UITheme.MUTED);
        mostrarPassword.setFocusPainted(false);
        mostrarPassword.addActionListener(event ->
                passwordField.setEchoChar(mostrarPassword.isSelected() ? (char) 0 : '●')
        );
        constraints.gridy = 6;
        constraints.insets = new Insets(0, 0, 18, 0);
        card.add(mostrarPassword, constraints);

        constraints.gridy = 7;
        constraints.insets = new Insets(0, 0, 10, 0);
        card.add(entrarButton, constraints);

        JButton registrarseButton = UITheme.botonSecundario("Crear una cuenta");
        registrarseButton.addActionListener(event -> abrirRegistro());
        constraints.gridy = 8;
        constraints.insets = new Insets(0, 0, 0, 0);
        card.add(registrarseButton, constraints);

        GridBagConstraints wrapperConstraints = new GridBagConstraints();
        wrapperConstraints.fill = GridBagConstraints.BOTH;
        wrapperConstraints.weightx = 1;
        wrapperConstraints.weighty = 1;
        wrapper.add(card, wrapperConstraints);

        return wrapper;
    }

    private JLabel etiqueta(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.LEFT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT);
        return label;
    }

    private void iniciarSesion(ActionEvent event) {
        char[] password = passwordField.getPassword();
        entrarButton.setEnabled(false);

        try {
            Usuario usuario = usuarioService.iniciarSesion(usuarioField.getText(), password);
            SesionUsuario.getInstance().iniciar(usuario);

            dispose();
            new DashboardFrame(usuarioService).setVisible(true);
        } catch (Exception exception) {
            Dialogs.error(this, exception);
            passwordField.setText("");
            passwordField.requestFocusInWindow();
        } finally {
            Arrays.fill(password, '\0');
            entrarButton.setEnabled(true);
        }
    }

    private void abrirRegistro() {
        UsuarioFormDialog dialog = new UsuarioFormDialog(
                this,
                usuarioService,
                UsuarioFormDialog.Modo.REGISTRO,
                null,
                () -> usuarioField.requestFocusInWindow()
        );
        dialog.setVisible(true);
    }
}
