package com.example.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encriptar(String contrasena) {
        return encoder.encode(contrasena);
    }

    public static boolean verificar(String contrasena, String hash) {
        return encoder.matches(contrasena, hash);
    }

    public static void main(String[] args) {
        String[] contrasenas = { "1234", "terapia456", "admin789", "p", "t", "a" };

        for (String pass : contrasenas) {
            System.out.println("Original: " + pass);
            System.out.println("Hash: " + encriptar(pass));
            System.out.println("------------------------------------");
        }
    }
}
