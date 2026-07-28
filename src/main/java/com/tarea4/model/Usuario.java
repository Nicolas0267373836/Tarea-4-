package com.tarea4.model;

/* Herencia: Usuario recibe los datos de Persona. */
public class Usuario extends Persona {

    private String usuario;
    private String passwordHash;

    @Override
    public String tipoPersona() {
        return "Usuario registrado";
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
