package com.tarea4.session;

import com.tarea4.model.Usuario;

import java.util.Optional;

/**
 * Singleton que conserva al usuario autenticado durante la ejecución.
 */
public final class SesionUsuario {

    private static final SesionUsuario INSTANCIA = new SesionUsuario();

    private Usuario usuarioActual;

    private SesionUsuario() {
    }

    public static SesionUsuario getInstance() {
        return INSTANCIA;
    }

    public void iniciar(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void cerrar() {
        this.usuarioActual = null;
    }

    public Optional<Usuario> getUsuarioActual() {
        return Optional.ofNullable(usuarioActual);
    }
}

