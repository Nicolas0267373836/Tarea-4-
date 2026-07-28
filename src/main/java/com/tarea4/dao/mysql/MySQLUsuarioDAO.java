package com.tarea4.dao.mysql;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.database.DatabaseConnection;
import com.tarea4.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación MySQL del repositorio de usuarios.
 * Todas las consultas usan PreparedStatement para evitar inyección SQL.
 */
public final class MySQLUsuarioDAO implements UsuarioDAO {

    private static final String COLUMNAS =
            "id, usuario, nombre, apellido, telefono, correo, password_hash";

    private final DatabaseConnection database;

    public MySQLUsuarioDAO(DatabaseConnection database) {
        this.database = database;
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM usuarios ORDER BY nombre, apellido, id";
        List<Usuario> usuarios = new ArrayList<>();

        try (
                Connection connection = database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                usuarios.add(mapear(resultSet));
            }
        }

        return usuarios;
    }

    @Override
    public Optional<Usuario> buscarPorUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM usuarios WHERE usuario = ?";

        try (
                Connection connection = database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nombreUsuario);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapear(resultSet))
                        : Optional.empty();
            }
        }
    }

    @Override
    public boolean existeUsuario(String nombreUsuario, Long idExcluido) throws SQLException {
        return existeValor("usuario", nombreUsuario, idExcluido);
    }

    @Override
    public boolean existeCorreo(String correo, Long idExcluido) throws SQLException {
        return existeValor("correo", correo, idExcluido);
    }

    private boolean existeValor(String columna, String valor, Long idExcluido) throws SQLException {
        // "columna" solo recibe constantes internas, nunca datos escritos por el usuario.
        String sql = "SELECT 1 FROM usuarios WHERE " + columna + " = ?"
                + (idExcluido == null ? "" : " AND id <> ?")
                + " LIMIT 1";

        try (
                Connection connection = database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, valor);
            if (idExcluido != null) {
                statement.setLong(2, idExcluido);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public long insertar(Usuario usuario) throws SQLException {
        String sql = """
                INSERT INTO usuarios
                    (usuario, nombre, apellido, telefono, correo, password_hash)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, usuario.getUsuario());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getApellido());
            statement.setString(4, usuario.getTelefono());
            statement.setString(5, usuario.getCorreo());
            statement.setString(6, usuario.getPasswordHash());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        }

        throw new SQLException("MySQL no devolvió el identificador del nuevo usuario.");
    }

    @Override
    public boolean actualizar(Usuario usuario, boolean actualizarPassword) throws SQLException {
        String sqlConPassword = """
                UPDATE usuarios
                SET usuario = ?, nombre = ?, apellido = ?, telefono = ?, correo = ?,
                    password_hash = ?
                WHERE id = ?
                """;
        String sqlSinPassword = """
                UPDATE usuarios
                SET usuario = ?, nombre = ?, apellido = ?, telefono = ?, correo = ?
                WHERE id = ?
                """;

        try (
                Connection connection = database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        actualizarPassword ? sqlConPassword : sqlSinPassword
                )
        ) {
            statement.setString(1, usuario.getUsuario());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getApellido());
            statement.setString(4, usuario.getTelefono());
            statement.setString(5, usuario.getCorreo());

            if (actualizarPassword) {
                statement.setString(6, usuario.getPasswordHash());
                statement.setLong(7, usuario.getId());
            } else {
                statement.setLong(6, usuario.getId());
            }

            return statement.executeUpdate() == 1;
        }
    }

    @Override
    public boolean eliminar(long id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (
                Connection connection = database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    private Usuario mapear(ResultSet resultSet) throws SQLException {
        return new Usuario(
                resultSet.getLong("id"),
                resultSet.getString("usuario"),
                resultSet.getString("nombre"),
                resultSet.getString("apellido"),
                resultSet.getString("telefono"),
                resultSet.getString("correo"),
                resultSet.getString("password_hash")
        );
    }
}

