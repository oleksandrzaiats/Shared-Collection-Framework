package se.lnu.application.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHelper {

    public static String encodePassword(String password) {

        return new BCryptPasswordEncoder().encode(password);
    }

    public static boolean checkPassword(String hashedPassword, String expectedPassword) {

        return new BCryptPasswordEncoder().matches(expectedPassword, hashedPassword);
    }
}