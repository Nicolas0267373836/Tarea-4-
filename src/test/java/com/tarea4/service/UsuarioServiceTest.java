package com.tarea4.service;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.exception.ValidationException;
import com.tarea4.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioServiceTest {

    private FakeUsuarioDAO dao;
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        dao = new FakeUsuarioDAO();
        service = new UsuarioService(dao);
    }

    @Test
    void registroExigeTodosLosCampos() {
        Usuario usuario = usuarioValido();
        usuario.setTelefono("");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.registrar(
                        usuario,
                        "ClaveSegura123".toCharArray(),
                        "ClaveSegura123".toCharArray()
                )
        );

        assertTrue(exception.getMessage().contains("número de teléfono"));
    }

    @Test
    void registroRechazaPasswordsDistintas() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.registrar(
                        usuarioValido(),
                        "ClaveSegura123".toCharArray(),
                        "OtraClave123".toCharArray()
                )
        );

        assertTrue(exception.getMessage().contains("no coinciden"));
    }

    @Test
    void registraYPermiteIniciarSesion() throws Exception {
        Usuario usuario = usuarioValido();
        long id = service.registrar(
                usuario,
                "ClaveSegura123".toCharArray(),
                "ClaveSegura123".toCharArray()
        );

        assertEquals(1L, id);
        assertNotNull(usuario.getPasswordHash());
        assertTrue(usuario.getPasswordHash().startsWith("pbkdf2_sha256$"));

        Usuario autenticado = service.iniciarSesion(
                "ana.perez",
                "ClaveSegura123".toCharArray()
        );
        assertEquals("Ana", autenticado.getNombre());
    }

    @Test
    void impideUsuarioDuplicado() throws Exception {
        service.registrar(
                usuarioValido(),
                "ClaveSegura123".toCharArray(),
                "ClaveSegura123".toCharArray()
        );

        Usuario repetido = usuarioValido();
        repetido.setCorreo("otro@correo.com");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.registrar(
                        repetido,
                        "ClaveSegura123".toCharArray(),
                        "ClaveSegura123".toCharArray()
                )
        );
        assertTrue(exception.getMessage().contains("usuario ya está registrado"));
    }

    private Usuario usuarioValido() {
        return new Usuario(
                null,
                "ana.perez",
                "Ana",
                "Pérez",
                "70012345",
                "ana@correo.com",
                null
        );
    }

    private static final class FakeUsuarioDAO implements UsuarioDAO {

        private final AtomicLong sequence = new AtomicLong(1);
        private final List<Usuario> usuarios = new ArrayList<>();

        @Override
        public List<Usuario> listarTodos() {
            return new ArrayList<>(usuarios);
        }

        @Override
        public Optional<Usuario> buscarPorUsuario(String nombreUsuario) {
            return usuarios.stream()
                    .filter(usuario -> usuario.getUsuario().equals(nombreUsuario))
                    .findFirst();
        }

        @Override
        public boolean existeUsuario(String nombreUsuario, Long idExcluido) {
            return usuarios.stream().anyMatch(usuario ->
                    usuario.getUsuario().equals(nombreUsuario)
                            && !usuario.getId().equals(idExcluido)
            );
        }

        @Override
        public boolean existeCorreo(String correo, Long idExcluido) {
            return usuarios.stream().anyMatch(usuario ->
                    usuario.getCorreo().equals(correo)
                            && !usuario.getId().equals(idExcluido)
            );
        }

        @Override
        public long insertar(Usuario usuario) {
            usuario.setId(sequence.getAndIncrement());
            usuarios.add(usuario);
            return usuario.getId();
        }

        @Override
        public boolean actualizar(Usuario usuario, boolean actualizarPassword) {
            for (int index = 0; index < usuarios.size(); index++) {
                if (usuarios.get(index).getId().equals(usuario.getId())) {
                    usuarios.set(index, usuario);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean eliminar(long id) {
            return usuarios.removeIf(usuario -> usuario.getId() == id);
        }
    }
}

