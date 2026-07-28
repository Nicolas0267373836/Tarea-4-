package com.tarea4.factory;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.dao.mysql.MySQLUsuarioDAO;
import com.tarea4.database.DatabaseConnection;

/**
 * Patrón Factory: centraliza la creación de repositorios.
 */
public final class DAOFactory {

    public enum TipoBaseDatos {
        MYSQL
    }

    private DAOFactory() {
    }

    public static UsuarioDAO crearUsuarioDAO(TipoBaseDatos tipo) {
        if (tipo == TipoBaseDatos.MYSQL) {
            return new MySQLUsuarioDAO(DatabaseConnection.getInstance());
        }
        throw new IllegalArgumentException("Tipo de base de datos no soportado: " + tipo);
    }
}

