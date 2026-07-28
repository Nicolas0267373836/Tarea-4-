package com.tarea4;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.factory.DAOFactory;
import com.tarea4.service.UsuarioService;
import com.tarea4.ui.LoginFrame;
import com.tarea4.ui.theme.UITheme;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación.
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UITheme.aplicar();

                // Factory: la interfaz usa UsuarioDAO sin depender de su implementación MySQL.
                UsuarioDAO usuarioDAO = DAOFactory.crearUsuarioDAO(DAOFactory.TipoBaseDatos.MYSQL);
                UsuarioService usuarioService = new UsuarioService(usuarioDAO);

                new LoginFrame(usuarioService).setVisible(true);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        null,
                        "No fue posible iniciar la aplicación.\n\n" + exception.getMessage(),
                        "Error de inicio",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}

