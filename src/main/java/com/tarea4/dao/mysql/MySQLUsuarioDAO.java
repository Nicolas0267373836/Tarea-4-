package com.tarea4.dao.mysql;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.database.DatabaseConnection;
import com.tarea4.model.Usuario;
import com.tarea4.security.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySQLUsuarioDAO implements UsuarioDAO {

    private Usuario crearUsuario(ResultSet result) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(result.getInt("id"));
        usuario.setUsuario(result.getString("usuario"));
        usuario.setNombre(result.getString("nombre"));
        usuario.setApellido(result.getString("apellido"));
        usuario.setTelefono(result.getString("telefono"));
        usuario.setCorreo(result.getString("correo"));
        usuario.setPasswordHash(result.getString("password_hash"));
        return usuario;
    }

    @Override
    public Usuario iniciarSesion(String nombreUsuario, String password) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE usuario = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombreUsuario);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Usuario usuario = crearUsuario(result);
                if (PasswordHasher.verificar(password.toCharArray(), usuario.getPasswordHash())) {
                    return usuario;
                }
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listarUsuarios() throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                usuarios.add(crearUsuario(result));
            }
        }
        return usuarios;
    }

    @Override
    public void registrar(Usuario usuario, String password) throws Exception {
        String sql = "INSERT INTO usuarios(usuario, nombre, apellido, telefono, correo, password_hash) VALUES(?,?,?,?,?,?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getUsuario());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getApellido());
            statement.setString(4, usuario.getTelefono());
            statement.setString(5, usuario.getCorreo());
            statement.setString(6, PasswordHasher.hash(password.toCharArray()));
            statement.executeUpdate();
        }
    }

    @Override
    public void actualizar(Usuario usuario, String nuevaPassword) throws Exception {
        boolean cambiarPassword = nuevaPassword != null && !nuevaPassword.isBlank();
        String sql = cambiarPassword
                ? "UPDATE usuarios SET usuario=?, nombre=?, apellido=?, telefono=?, correo=?, password_hash=? WHERE id=?"
                : "UPDATE usuarios SET usuario=?, nombre=?, apellido=?, telefono=?, correo=? WHERE id=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getUsuario());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getApellido());
            statement.setString(4, usuario.getTelefono());
            statement.setString(5, usuario.getCorreo());
            if (cambiarPassword) {
                statement.setString(6, PasswordHasher.hash(nuevaPassword.toCharArray()));
                statement.setInt(7, usuario.getId());
            } else {
                statement.setInt(6, usuario.getId());
            }
            statement.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM usuarios WHERE id=?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
