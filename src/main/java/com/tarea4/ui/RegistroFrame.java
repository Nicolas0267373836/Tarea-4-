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

public class RegistroFrame extends JFrame {

    private UsuarioDAO dao;
    private JTextField usuario = new JTextField();
    private JTextField nombre = new JTextField();
    private JTextField apellido = new JTextField();
    private JTextField telefono = new JTextField();
    private JTextField correo = new JTextField();
    private JPasswordField password = new JPasswordField();
    private JPasswordField confirmar = new JPasswordField();

    public RegistroFrame(UsuarioDAO dao) {
        this.dao = dao;
        setTitle("Registro de usuario");
        setSize(420, 480);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(15, 1, 5, 5));
        agregarCampo(panel, "Nombre de usuario:", usuario);
        agregarCampo(panel, "Nombre:", nombre);
        agregarCampo(panel, "Apellido:", apellido);
        agregarCampo(panel, "Número de teléfono:", telefono);
        agregarCampo(panel, "Correo electrónico:", correo);
        agregarCampo(panel, "Contraseña:", password);
        agregarCampo(panel, "Confirmar contraseña:", confirmar);

        JButton guardar = new JButton("Registrar");
        guardar.addActionListener(event -> registrar());
        panel.add(guardar);
        add(panel);
    }

    private void agregarCampo(JPanel panel, String texto, JTextField campo) {
        panel.add(new JLabel(texto));
        panel.add(campo);
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
