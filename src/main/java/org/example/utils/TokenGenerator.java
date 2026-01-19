package org.example.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;

public class TokenGenerator {
    private static final int TOKEN_BYTE_LENGTH = 16;
    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTE_LENGTH];
        random.nextBytes(bytes);
        return Hex.encodeHexString(bytes).toUpperCase();
    }
}
