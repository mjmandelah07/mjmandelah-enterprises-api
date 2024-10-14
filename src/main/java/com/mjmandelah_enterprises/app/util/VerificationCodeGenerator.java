package com.mjmandelah_enterprises.app.util;

import java.util.Random;

public class VerificationCodeGenerator {
    private static final Random RANDOM = new Random();

    public static String generateCode() {
        return String.format("%06d", RANDOM.nextInt(1000000)); // Generates a 6-digit code
    }
}
