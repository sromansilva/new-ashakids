package com.example.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenerarHashTest {

    @Test
    void testEncriptarGeneraHash() {
        String original = "1234";
        String hash = GenerarHash.encriptar(original);

        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void testVerificarContrasenaCorrecta() {
        String original = "terapia456";
        String hash = GenerarHash.encriptar(original);

        assertTrue(GenerarHash.verificar(original, hash));
    }

    @Test
    void testVerificarContrasenaIncorrecta() {
        String original = "admin789";
        String hash = GenerarHash.encriptar(original);

        assertFalse(GenerarHash.verificar("otraClave", hash));
    }

    @Test
    void testHashesDistintosParaMismaContrasena() {
        String pass = "p";

        String hash1 = GenerarHash.encriptar(pass);
        String hash2 = GenerarHash.encriptar(pass);

        assertNotEquals(hash1, hash2, "Los hashes deben ser distintos por el salt aleatorio");
    }
}
