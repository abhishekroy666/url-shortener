package io.github.abhishekroy666.urlshortener.util;

/**
 * Utility class for Base62 encoding and decoding.
 * <p>
 * The encoder maps non-negative long IDs to a compact Base62
 * string using the characters a-zA-Z0-9. This is useful for
 * creating short tokens from numeric database IDs.
 * </p>
 */
public class Base62Encoder {
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALLOWED_CHARACTERS.length();

    /**
     * Encode a non-negative long value to a Base62 string.
     *
     * @param input non-negative long value to encode
     * @return Base62 representation of the input value
     */
    public static String encode(long input) {
        StringBuilder encodedString = new StringBuilder();
        if (input == 0) return String.valueOf(ALLOWED_CHARACTERS.charAt(0));
        while (input > 0) {
            encodedString.append(ALLOWED_CHARACTERS.charAt((int) (input % BASE)));
            input /= BASE;
        }
        return encodedString.reverse().toString();
    }

    /**
     * Decode a Base62 string back to the long value.
     *
     * @param input Base62-encoded string
     * @return decoded long value
     * @throws IllegalArgumentException if the input contains unsupported characters
     */
    public static long decode(String input) {
        long decodedLong = 0;
        for (int i = 0; i < input.length(); i++) {
            decodedLong = decodedLong * BASE + ALLOWED_CHARACTERS.indexOf(input.charAt(i));
        }
        return decodedLong;
    }
}
