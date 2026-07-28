package com.tarea4.model;

/**
 * Abstracción de una persona.
 *
 * Sus atributos son privados (encapsulamiento) y Usuario hereda de esta clase.
 */
public abstract class Persona {

    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;

    protected Persona() {
    }

    protected Persona(Long id, String nombre, String apellido, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
    }

    public abstract String getTipoPersona();

    public String getNombreCompleto() {
        return (nombre + " " + apellido).trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}

