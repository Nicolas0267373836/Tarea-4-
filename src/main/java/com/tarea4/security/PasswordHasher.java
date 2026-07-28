package com.tarea4.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Protege las contraseñas con PBKDF2, sal aleatoria y comparación segura.
 * Nunca se guarda ni se consulta una contraseña en texto plano.
 */
public final class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String PREFIX = "pbkdf2_sha256";
    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_LENGTH_BYTES = 16;

    private PasswordHasher() {
    }

    public static String hash(char[] password) {
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = derivar(password, salt, ITERATIONS);

        try {
            return PREFIX
                    + "$" + ITERATIONS
                    + "$" + Base64.getEncoder().encodeToString(salt)
                    + "$" + Base64.getEncoder().encodeToString(hash);
        } finally {
            Arrays.fill(hash, (byte) 0);
            Arrays.fill(salt, (byte) 0);
        }
    }

    public static boolean verificar(char[] password, String hashGuardado) {
        if (hashGuardado == null) {
            return false;
        }

        String[] partes = hashGuardado.split("\\$");
        if (partes.length != 4 || !PREFIX.equals(partes[0])) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(partes[1]);
            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            byte[] calculado = derivar(password, salt, iterations);

            try {
                return MessageDigest.isEqual(esperado, calculado);
            } finally {
                Arrays.fill(salt, (byte) 0);
                Arrays.fill(esperado, (byte) 0);
                Arrays.fill(calculado, (byte) 0);
            }
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private static byte[] derivar(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH_BITS);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM)
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("No se pudo proteger la contraseña.", exception);
        } finally {
            spec.clearPassword();
        }
    }
}

