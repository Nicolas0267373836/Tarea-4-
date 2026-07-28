package com.tarea4.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lee la configuración desde database.properties y permite sobrescribirla
 * mediante variables de entorno.
 */
public final class DatabaseConfig {

    private static final String CONFIG_FILE = "/database.properties";

    private final String url;
    private final String usuario;
    private final String password;

    private DatabaseConfig(String url, String usuario, String password) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    public static DatabaseConfig cargar() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("No se encontró el archivo database.properties.");
            }
            properties.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo leer database.properties.", exception);
        }

        String url = obtenerValor("TAREA4_DB_URL", properties.getProperty("db.url"));
        String usuario = obtenerValor("TAREA4_DB_USER", properties.getProperty("db.user"));
        String password = obtenerValor("TAREA4_DB_PASSWORD", properties.getProperty("db.password", ""));

        if (esVacio(url) || esVacio(usuario)) {
            throw new IllegalStateException("La URL y el usuario de MySQL son obligatorios.");
        }

        return new DatabaseConfig(url.trim(), usuario.trim(), password);
    }

    private static String obtenerValor(String variableEntorno, String valorPorDefecto) {
        String valorEntorno = System.getenv(variableEntorno);
        return esVacio(valorEntorno) ? valorPorDefecto : valorEntorno;
    }

    private static boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }
}

