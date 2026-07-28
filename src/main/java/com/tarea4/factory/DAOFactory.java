package com.tarea4.factory;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.dao.mysql.MySQLUsuarioDAO;

/* Factory: crea el DAO que usa la aplicación. */
public class DAOFactory {

    public static UsuarioDAO crearUsuarioDAO() {
        return new MySQLUsuarioDAO();
    }
}
