package org.example.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
    public static boolean checkPassword(String plainPassword, String storedHash) {
        return passwordEncoder.matches(plainPassword, storedHash);
    }
}
