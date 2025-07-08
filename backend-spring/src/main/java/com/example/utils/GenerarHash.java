package com.example.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String[] contrasenas = { "clave123", "terapia456", "admin789", "p", "t", "a" };

        for (String pass : contrasenas) {
            System.out.println("Original: " + pass);
            System.out.println("Hash: " + encoder.encode(pass));
            System.out.println("------------------------------------");
        }
    }
}
