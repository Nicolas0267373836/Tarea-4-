package com.tarea4.dao;

import com.tarea4.model.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Abstracción de acceso a datos. La interfaz no conoce MySQL ni Swing.
 */
public interface UsuarioDAO {

    List<Usuario> listarTodos() throws SQLException;

    Optional<Usuario> buscarPorUsuario(String nombreUsuario) throws SQLException;

    boolean existeUsuario(String nombreUsuario, Long idExcluido) throws SQLException;

    boolean existeCorreo(String correo, Long idExcluido) throws SQLException;

    long insertar(Usuario usuario) throws SQLException;

    boolean actualizar(Usuario usuario, boolean actualizarPassword) throws SQLException;

    boolean eliminar(long id) throws SQLException;
}

