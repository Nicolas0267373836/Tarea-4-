package com.tarea4;

import com.tarea4.factory.DAOFactory;
import com.tarea4.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(DAOFactory.crearUsuarioDAO());
            login.setVisible(true);
        });
    }
}
