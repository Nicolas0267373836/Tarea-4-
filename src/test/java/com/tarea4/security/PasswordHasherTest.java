package com.tarea4.security;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    @Test
    void creaHashesDiferentesYVerificablesParaLaMismaPassword() {
        char[] password = "ClaveSegura123".toCharArray();

        try {
            String primerHash = PasswordHasher.hash(password);
            String segundoHash = PasswordHasher.hash(password);

            assertNotEquals(primerHash, segundoHash, "Cada hash debe usar una sal distinta");
            assertTrue(PasswordHasher.verificar(password, primerHash));
            assertTrue(PasswordHasher.verificar(password, segundoHash));
            assertFalse(PasswordHasher.verificar("Incorrecta".toCharArray(), primerHash));
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}

