package com.mjmandelah_enterprises.app.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[24]; // Adjust length as needed
        secureRandom.nextBytes(randomBytes);
        String secretKey = Base64.getEncoder().encodeToString(randomBytes);
        System.out.println("Generated Secret Key: " + secretKey);
    }
}
