package com.tarea4.dao;

import com.tarea4.model.Usuario;

import java.util.List;

/* Interfaz: la ventana usa estas operaciones sin conocer el SQL. */
public interface UsuarioDAO {

    Usuario iniciarSesion(String nombreUsuario, String password) throws Exception;
    List<Usuario> listarUsuarios() throws Exception;
    void registrar(Usuario usuario, String password) throws Exception;
    void actualizar(Usuario usuario, String nuevaPassword) throws Exception;
    void eliminar(int id) throws Exception;
}
