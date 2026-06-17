package io.github.abhishekroy666.url_shortener.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Base62Encoder utility.
 * Tests encoding and decoding of long values to/from Base62 format.
 * Provides comprehensive coverage of all branches and edge cases.
 */
class Base62EncoderTest {

    @Test
    void encode_zeroValue_returnsFirstCharacter() {
        assertEquals("a", Base62Encoder.encode(0));
    }

    @Test
    void encode_oneValue_returnsSecondCharacter() {
        assertEquals("b", Base62Encoder.encode(1));
    }

    @Test
    void encode_smallPositiveValue_returnsBase62String() {
        assertEquals("k", Base62Encoder.encode(10));
    }

    @Test
    void encode_baseEquivalentValue_returnsTwoCharacters() {
        assertEquals("ba", Base62Encoder.encode(62));
    }

    @Test
    void encode_largeValue_returnsMultipleCharacters() {
        assertEquals("99", Base62Encoder.encode(3843));
    }

    @Test
    void encode_veryLargeValue_encodesCorrectly() {
        String result = Base62Encoder.encode(1000000);
        assertNotNull(result);
        assertFalse(result.isBlank());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    void encode_maxLongValue_encodesSuccessfully() {
        String result = Base62Encoder.encode(Long.MAX_VALUE);
        assertNotNull(result);
        assertFalse(result.isBlank());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    void decode_singleCharacter_returnsCorrectValue() {
        assertEquals(0, Base62Encoder.decode("a"));
    }

    @Test
    void decode_secondCharacter_returnsOne() {
        assertEquals(1, Base62Encoder.decode("b"));
    }

    @Test
    void decode_multipleCharacters_returnsCorrectValue() {
        assertEquals(10, Base62Encoder.decode("k"));
    }

    @Test
    void decode_twoCharacterValue_decodesCorrectly() {
        assertEquals(62, Base62Encoder.decode("ba"));
    }

    @Test
    void decode_threeCharacterValue_decodesCorrectly() {
        assertEquals(3907, Base62Encoder.decode("bbb"));
    }

    @Test
    void decode_largeEncodedValue_decodesCorrectly() {
        String encoded = Base62Encoder.encode(1000000);
        long result = Base62Encoder.decode(encoded);
        assertEquals(1000000, result);
    }

    @Test
    void decode_emptyString_returnsZero() {
        assertEquals(0L, Base62Encoder.decode(""));
    }

    @Test
    void decode_unsupportedCharacter_handlesGracefully() {
        // indexOf returns -1 when character not found; this gets added to decodedLong
        long result = Base62Encoder.decode("!");
        assertEquals(-1L, result);
    }

    @Test
    void roundTrip_encodeAndDecode_returnsOriginalValue() {
        long originalValue = 12345;
        String encoded = Base62Encoder.encode(originalValue);
        long decoded = Base62Encoder.decode(encoded);
        assertEquals(originalValue, decoded);
    }

    @Test
    void roundTrip_withZero_returnsOriginalValue() {
        long originalValue = 0;
        String encoded = Base62Encoder.encode(originalValue);
        long decoded = Base62Encoder.decode(encoded);
        assertEquals(originalValue, decoded);
    }

    @Test
    void roundTrip_withMaxValue_returnsOriginalValue() {
        long originalValue = Long.MAX_VALUE;
        String encoded = Base62Encoder.encode(originalValue);
        long decoded = Base62Encoder.decode(encoded);
        assertEquals(originalValue, decoded);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 10, 61, 62, 100, 1000, 10000, 1000000, 123456789L})
    void roundTrip_multipleValues_allReturnOriginal(long value) {
        String encoded = Base62Encoder.encode(value);
        long decoded = Base62Encoder.decode(encoded);
        assertEquals(value, decoded, "Roundtrip failed for value: " + value);
    }

    @Test
    void encode_allValidCharacterRange_includeOnlyAllowedChars() {
        for (long i = 0; i < 5000; i++) {
            String encoded = Base62Encoder.encode(i);
            assertTrue(encoded.matches("[a-zA-Z0-9]+"), 
                "Encoded value should only contain valid Base62 characters: " + encoded);
        }
    }

    @Test
    void encode_consecutiveValues_produceDifferentTokens() {
        String token1 = Base62Encoder.encode(1);
        String token2 = Base62Encoder.encode(2);
        assertNotEquals(token1, token2);
    }

    @Test
    void encode_and_decode_largeSequence_allCorrect() {
        for (long i = 0; i < 100; i++) {
            long value = i * 1000;
            String encoded = Base62Encoder.encode(value);
            long decoded = Base62Encoder.decode(encoded);
            assertEquals(value, decoded);
        }
    }

}
