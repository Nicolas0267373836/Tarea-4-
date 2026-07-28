package com.tarea4.ui;

import com.tarea4.model.Usuario;
import com.tarea4.service.UsuarioService;
import com.tarea4.ui.components.Dialogs;
import com.tarea4.ui.theme.UITheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

/**
 * Formulario reutilizable para registro, creación y actualización.
 */
public final class UsuarioFormDialog extends JDialog {

    public enum Modo {
        REGISTRO,
        NUEVO,
        EDICION
    }

    private final UsuarioService usuarioService;
    private final Modo modo;
    private final Usuario usuarioOriginal;
    private final Runnable alGuardar;

    private final JTextField usuarioField = new JTextField();
    private final JTextField nombreField = new JTextField();
    private final JTextField apellidoField = new JTextField();
    private final JTextField telefonoField = new JTextField();
    private final JTextField correoField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmarPasswordField = new JPasswordField();
    private final JButton guardarButton = UITheme.botonPrimario("Guardar");

    public UsuarioFormDialog(
            JFrame owner,
            UsuarioService usuarioService,
            Modo modo,
            Usuario usuario,
            Runnable alGuardar
    ) {
        super(owner, true);
        this.usuarioService = usuarioService;
        this.modo = modo;
        this.usuarioOriginal = usuario;
        this.alGuardar = alGuardar;

        setTitle(obtenerTitulo());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(590, 700);
        setMinimumSize(new Dimension(510, 620));
        setLocationRelativeTo(owner);
        setContentPane(crearContenido());
        getRootPane().setDefaultButton(guardarButton);

        cargarDatos();
        guardarButton.addActionListener(event -> guardar());
    }

    private JPanel crearContenido() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BACKGROUND);

        JPanel header = new JPanel();
        header.setBackground(UITheme.NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(obtenerTitulo());
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);

        JLabel subtitle = new JLabel(obtenerSubtitulo());
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new java.awt.Color(203, 213, 225));
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(17, 30, 17, 30));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        int fila = 0;
        fila = agregarCampo(form, constraints, fila, "Nombre de usuario *", usuarioField);
        fila = agregarCampo(form, constraints, fila, "Nombre *", nombreField);
        fila = agregarCampo(form, constraints, fila, "Apellido *", apellidoField);
        fila = agregarCampo(form, constraints, fila, "Número de teléfono *", telefonoField);
        fila = agregarCampo(form, constraints, fila, "Correo electrónico *", correoField);
        fila = agregarCampo(
                form,
                constraints,
                fila,
                modo == Modo.EDICION ? "Nueva contraseña (opcional)" : "Contraseña *",
                passwordField
        );
        fila = agregarCampo(
                form,
                constraints,
                fila,
                modo == Modo.EDICION
                        ? "Confirmar nueva contraseña"
                        : "Confirmar contraseña *",
                confirmarPasswordField
        );

        passwordField.setEchoChar('●');
        confirmarPasswordField.setEchoChar('●');

        JCheckBox mostrar = new JCheckBox("Mostrar contraseñas");
        mostrar.setBackground(UITheme.WHITE);
        mostrar.setForeground(UITheme.MUTED);
        mostrar.setFocusPainted(false);
        mostrar.addActionListener(event -> {
            char echo = mostrar.isSelected() ? (char) 0 : '●';
            passwordField.setEchoChar(echo);
            confirmarPasswordField.setEchoChar(echo);
        });
        constraints.gridy = fila++;
        constraints.insets = new Insets(0, 0, 16, 0);
        form.add(mostrar, constraints);

        if (modo == Modo.EDICION) {
            JLabel hint = new JLabel(
                    "<html>Deja las contraseñas vacías para conservar la actual.</html>"
            );
            hint.setForeground(UITheme.MUTED);
            hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            constraints.gridy = fila++;
            constraints.insets = new Insets(0, 0, 16, 0);
            form.add(hint, constraints);
        }

        JPanel actions = new JPanel();
        actions.setBackground(UITheme.WHITE);
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));

        JButton cancelButton = UITheme.botonSecundario("Cancelar");
        cancelButton.addActionListener(event -> dispose());
        actions.add(Box.createHorizontalGlue());
        actions.add(cancelButton);
        actions.add(Box.createHorizontalStrut(10));
        actions.add(guardarButton);

        constraints.gridy = fila;
        constraints.insets = new Insets(4, 0, 0, 0);
        form.add(actions, constraints);

        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        root.add(scrollPane, BorderLayout.CENTER);

        return root;
    }

    private int agregarCampo(
            JPanel panel,
            GridBagConstraints constraints,
            int fila,
            String etiqueta,
            javax.swing.JComponent campo
    ) {
        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT);

        constraints.gridy = fila++;
        constraints.insets = new Insets(0, 0, 4, 0);
        panel.add(label, constraints);

        UITheme.prepararCampo(campo);
        campo.setPreferredSize(new Dimension(320, 36));
        constraints.gridy = fila++;
        constraints.insets = new Insets(0, 0, 9, 0);
        panel.add(campo, constraints);

        return fila;
    }

    private void cargarDatos() {
        if (usuarioOriginal == null) {
            return;
        }

        usuarioField.setText(usuarioOriginal.getUsuario());
        nombreField.setText(usuarioOriginal.getNombre());
        apellidoField.setText(usuarioOriginal.getApellido());
        telefonoField.setText(usuarioOriginal.getTelefono());
        correoField.setText(usuarioOriginal.getCorreo());
    }

    private void guardar() {
        char[] password = passwordField.getPassword();
        char[] confirmacion = confirmarPasswordField.getPassword();
        guardarButton.setEnabled(false);

        try {
            Usuario usuario = crearUsuarioDesdeFormulario();

            if (modo == Modo.EDICION) {
                usuarioService.actualizar(usuario, password, confirmacion);
                Dialogs.exito(this, "Los datos se actualizaron correctamente.");
            } else {
                usuarioService.registrar(usuario, password, confirmacion);
                Dialogs.exito(
                        this,
                        modo == Modo.REGISTRO
                                ? "La cuenta fue creada. Ya puedes iniciar sesión."
                                : "El usuario fue registrado correctamente."
                );
            }

            dispose();
            if (alGuardar != null) {
                alGuardar.run();
            }
        } catch (Exception exception) {
            Dialogs.error(this, exception);
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirmacion, '\0');
            passwordField.setText("");
            confirmarPasswordField.setText("");
            guardarButton.setEnabled(true);
        }
    }

    private Usuario crearUsuarioDesdeFormulario() {
        Long id = usuarioOriginal == null ? null : usuarioOriginal.getId();
        String passwordHash = usuarioOriginal == null ? null : usuarioOriginal.getPasswordHash();

        return new Usuario(
                id,
                usuarioField.getText(),
                nombreField.getText(),
                apellidoField.getText(),
                telefonoField.getText(),
                correoField.getText(),
                passwordHash
        );
    }

    private String obtenerTitulo() {
        return switch (modo) {
            case REGISTRO -> "Crear una cuenta";
            case NUEVO -> "Registrar usuario";
            case EDICION -> "Actualizar usuario";
        };
    }

    private String obtenerSubtitulo() {
        if (modo == Modo.EDICION) {
            return "Modifica los datos necesarios y guarda los cambios.";
        }
        return "Completa todos los campos obligatorios.";
    }
}
