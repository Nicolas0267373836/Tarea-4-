package com.tarea4.database;

import com.tarea4.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Patrón Singleton: existe una sola instancia encargada de crear conexiones.
 * Se abre una conexión por operación para evitar reutilizar conexiones vencidas.
 */
public final class DatabaseConnection {

    private static volatile DatabaseConnection instancia;

    private final DatabaseConfig config;

    private DatabaseConnection() {
        config = DatabaseConfig.cargar();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(
                    "No se encontró MySQL Connector/J. Compila el proyecto con Maven.",
                    exception
            );
        }
    }

    public static DatabaseConnection getInstance() {
        if (instancia == null) {
            synchronized (DatabaseConnection.class) {
                if (instancia == null) {
                    instancia = new DatabaseConnection();
                }
            }
        }
        return instancia;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getUrl(),
                config.getUsuario(),
                config.getPassword()
        );
    }

    public void probarConexion() throws SQLException {
        try (Connection ignored = getConnection()) {
            // Si no se lanza excepción, la conexión está disponible.
        }
    }
}

