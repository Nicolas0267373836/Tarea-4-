package com.tarea4.service;

import com.tarea4.dao.UsuarioDAO;
import com.tarea4.exception.ValidationException;
import com.tarea4.model.Usuario;
import com.tarea4.security.PasswordHasher;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Contiene las reglas de negocio y validaciones de la aplicación.
 */
public final class UsuarioService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+()\\- ]{7,25}$");
    private static final Pattern USER_PATTERN = Pattern.compile("^[A-Za-z0-9._-]{3,50}$");

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario iniciarSesion(String nombreUsuario, char[] password)
            throws ValidationException, SQLException {
        String usuarioNormalizado = limpiar(nombreUsuario);

        if (esVacio(usuarioNormalizado) || password == null || password.length == 0) {
            throw new ValidationException(
                    "Debe ingresar su usuario y contraseña; "
                            + "si no está registrado, debe registrarse."
            );
        }

        Optional<Usuario> encontrado = usuarioDAO.buscarPorUsuario(usuarioNormalizado);
        if (encontrado.isEmpty()
                || !PasswordHasher.verificar(password, encontrado.get().getPasswordHash())) {
            throw new ValidationException("Usuario o contraseña incorrectos.");
        }

        return encontrado.get();
    }

    public long registrar(Usuario usuario, char[] password, char[] confirmacion)
            throws ValidationException, SQLException {
        validarDatosComunes(usuario);
        validarPasswordObligatorio(password, confirmacion);
        validarDuplicados(usuario);

        usuario.setPasswordHash(PasswordHasher.hash(password));
        return usuarioDAO.insertar(usuario);
    }

    public void actualizar(Usuario usuario, char[] password, char[] confirmacion)
            throws ValidationException, SQLException {
        if (usuario.getId() == null) {
            throw new ValidationException("No se encontró el identificador del usuario.");
        }

        validarDatosComunes(usuario);
        validarDuplicados(usuario);

        boolean cambiarPassword = tieneContenido(password) || tieneContenido(confirmacion);
        if (cambiarPassword) {
            validarPasswordObligatorio(password, confirmacion);
            usuario.setPasswordHash(PasswordHasher.hash(password));
        }

        if (!usuarioDAO.actualizar(usuario, cambiarPassword)) {
            throw new ValidationException("El usuario ya no existe o no pudo actualizarse.");
        }
    }

    public void eliminar(long id) throws ValidationException, SQLException {
        if (!usuarioDAO.eliminar(id)) {
            throw new ValidationException("El usuario ya no existe o no pudo eliminarse.");
        }
    }

    public List<Usuario> listarTodos() throws SQLException {
        return usuarioDAO.listarTodos();
    }

    private void validarDatosComunes(Usuario usuario)
            throws ValidationException {
        usuario.setUsuario(limpiar(usuario.getUsuario()));
        usuario.setNombre(limpiar(usuario.getNombre()));
        usuario.setApellido(limpiar(usuario.getApellido()));
        usuario.setTelefono(limpiar(usuario.getTelefono()));
        usuario.setCorreo(limpiar(usuario.getCorreo()).toLowerCase(Locale.ROOT));

        validarObligatorio(usuario.getUsuario(), "nombre de usuario");
        validarObligatorio(usuario.getNombre(), "nombre");
        validarObligatorio(usuario.getApellido(), "apellido");
        validarObligatorio(usuario.getTelefono(), "número de teléfono");
        validarObligatorio(usuario.getCorreo(), "correo electrónico");

        if (!USER_PATTERN.matcher(usuario.getUsuario()).matches()) {
            throw new ValidationException(
                    "El nombre de usuario debe tener entre 3 y 50 caracteres "
                            + "y solo puede contener letras, números, punto, guion o guion bajo."
            );
        }
        if (usuario.getNombre().length() > 80) {
            throw new ValidationException("El nombre no puede superar 80 caracteres.");
        }
        if (usuario.getApellido().length() > 80) {
            throw new ValidationException("El apellido no puede superar 80 caracteres.");
        }
        if (!PHONE_PATTERN.matcher(usuario.getTelefono()).matches()) {
            throw new ValidationException(
                    "El teléfono debe tener entre 7 y 25 caracteres y contener solo números, "
                            + "espacios, paréntesis, + o -."
            );
        }
        if (!EMAIL_PATTERN.matcher(usuario.getCorreo()).matches()
                || usuario.getCorreo().length() > 120) {
            throw new ValidationException("Debe ingresar un correo electrónico válido.");
        }
    }

    private void validarDuplicados(Usuario usuario)
            throws ValidationException, SQLException {
        if (usuarioDAO.existeUsuario(usuario.getUsuario(), usuario.getId())) {
            throw new ValidationException("El nombre de usuario ya está registrado.");
        }
        if (usuarioDAO.existeCorreo(usuario.getCorreo(), usuario.getId())) {
            throw new ValidationException("El correo electrónico ya está registrado.");
        }
    }

    private void validarPasswordObligatorio(char[] password, char[] confirmacion)
            throws ValidationException {
        if (!tieneContenido(password)) {
            throw new ValidationException("El campo contraseña es obligatorio.");
        }
        if (!tieneContenido(confirmacion)) {
            throw new ValidationException("El campo confirmar contraseña es obligatorio.");
        }
        if (password.length < 8) {
            throw new ValidationException("La contraseña debe tener al menos 8 caracteres.");
        }
        if (!Arrays.equals(password, confirmacion)) {
            throw new ValidationException("La contraseña y su confirmación no coinciden.");
        }
    }

    private void validarObligatorio(String valor, String nombreCampo)
            throws ValidationException {
        if (esVacio(valor)) {
            throw new ValidationException("El campo " + nombreCampo + " es obligatorio.");
        }
    }

    private boolean tieneContenido(char[] valor) {
        return valor != null && valor.length > 0;
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}

