package com.tarea4.ui.components;

import com.tarea4.exception.ValidationException;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.sql.SQLException;

/**
 * Mensajes consistentes y comprensibles para el usuario.
 */
public final class Dialogs {

    private Dialogs() {
    }

    public static void error(Component parent, Exception exception) {
        String mensaje;

        if (exception instanceof ValidationException) {
            mensaje = exception.getMessage();
        } else if (exception instanceof SQLException) {
            mensaje = """
                    No se pudo completar la operación en MySQL.

                    Verifica que:
                    1. MySQL esté encendido.
                    2. Ejecutaste database/schema.sql.
                    3. database.properties tenga el usuario y contraseña correctos.

                    Detalle técnico: %s
                    """.formatted(exception.getMessage());
        } else {
            mensaje = "Ocurrió un error inesperado.\n\nDetalle: " + exception.getMessage();
        }

        JOptionPane.showMessageDialog(
                parent,
                mensaje,
                "No se pudo completar la operación",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void exito(Component parent, String mensaje) {
        JOptionPane.showMessageDialog(
                parent,
                mensaje,
                "Operación completada",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

