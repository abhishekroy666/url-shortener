package io.github.abhishekroy666.url_shortener.util;

public class Base62Encoder {
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALLOWED_CHARACTERS.length();

    public static String encode(long input) {
        StringBuilder encodedString = new StringBuilder();
        if (input == 0) return String.valueOf(ALLOWED_CHARACTERS.charAt(0));
        while (input > 0) {
            encodedString.append(ALLOWED_CHARACTERS.charAt((int) (input % BASE)));
            input /= BASE;
        }
        return encodedString.reverse().toString();
    }

    public static long decode(String input) {
        long decodedLong = 0;
        for (int i = 0; i < input.length(); i++) {
            decodedLong = decodedLong * BASE + ALLOWED_CHARACTERS.indexOf(input.charAt(i));
        }
        return decodedLong;
    }
}
