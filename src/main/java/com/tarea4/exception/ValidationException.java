package com.tarea4.exception;

/**
 * Error de validación que puede mostrarse de forma segura al usuario.
 */
public final class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}

