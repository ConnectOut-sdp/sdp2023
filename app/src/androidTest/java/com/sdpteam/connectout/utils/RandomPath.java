package com.sdpteam.connectout.utils;

import java.util.Random;

public class RandomPath {
    private static final int DEFAULT_LENGTH = 16;
    private static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateRandomPath() {
        return generateRandomString(DEFAULT_LENGTH);
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
