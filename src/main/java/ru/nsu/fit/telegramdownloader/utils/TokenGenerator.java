package ru.nsu.fit.telegramdownloader.utils;

import java.security.SecureRandom;

// https://gist.github.com/davidadale/4075606
public class TokenGenerator {
    protected static SecureRandom random = new SecureRandom();

    private TokenGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateToken() {
        long longToken = Math.abs(random.nextLong() / 2);
        return Long.toString( longToken, 16 );
    }
}