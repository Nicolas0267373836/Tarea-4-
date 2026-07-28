package com.tarea4.model;

/**
 * Herencia: Usuario extiende Persona.
 * Polimorfismo: sobrescribe getTipoPersona().
 */
public final class Usuario extends Persona {

    private String usuario;
    private String passwordHash;

    public Usuario() {
    }

    public Usuario(
            Long id,
            String usuario,
            String nombre,
            String apellido,
            String telefono,
            String correo,
            String passwordHash
    ) {
        super(id, nombre, apellido, telefono, correo);
        this.usuario = usuario;
        this.passwordHash = passwordHash;
    }

    @Override
    public String getTipoPersona() {
        return "Usuario registrado";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}

