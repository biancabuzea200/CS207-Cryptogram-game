import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CryptogramTest {
    Cryptogram generateHelloWorld() {
        int[] mappedQuote = { 1, 2, 3, 3, 4, 0, 5, 4, 6, 3, 7 };
        Map<Integer, Character> mapping = new HashMap<>();
        mapping.put(1, 'H');
        mapping.put(2, 'E');
        mapping.put(3, 'L');
        mapping.put(4, 'O');
        mapping.put(5, 'W');
        mapping.put(6, 'R');
        mapping.put(7, 'D');
        return new Cryptogram(mappedQuote, mapping);
    }

    @Test
    void testMappingCreation() {
        Cryptogram cg = new Cryptogram("HELLO WORLD");

        // our cryptogram will have a random mapping so we cannot check that
        // but we can check that it meets the conditions set
        Map<Integer, Character> mapping = cg.getMapping();

        // there should be 7 distinct entries
        assertEquals(7, mapping.size());

        // there shouldn't be any characters mapped to themselves
        for (Map.Entry<Integer, Character> e : mapping.entrySet()) {
            char c = (char)((e.getKey() - 1) + 'A');
            assertNotEquals(c, e.getValue());
        }

        // we can check if it's valid by putting the mapped quote and the
        // mapping into a new Cryptogram instance and seeing if it can get
        // the same original quote back
        Cryptogram cg2 = new Cryptogram(cg.getMappedQuote(), mapping);
        assertEquals("HELLO WORLD", cg2.getOriginalQuote());
    }

    @Test
    void getFrequencies() {
        Cryptogram cg = generateHelloWorld();
        Map<Integer, Integer> frequencies = cg.getFrequencies();

        assertEquals(7, frequencies.size());
        assertEquals(1, (int)frequencies.get(1));
        assertEquals(1, (int)frequencies.get(2));
        assertEquals(3, (int)frequencies.get(3));
        assertEquals(2, (int)frequencies.get(4));
        assertEquals(1, (int)frequencies.get(5));
        assertEquals(1, (int)frequencies.get(6));
        assertEquals(1, (int)frequencies.get(7));
    }

    @Test
    void getOriginalQuote() {
        Cryptogram cg = generateHelloWorld();
        assertEquals("HELLO WORLD", cg.getOriginalQuote());
    }

    @Test
    void encodeMappingToString() {
        Map<Integer, Character> decoded = new HashMap<>();
        decoded.put(3, 'A');
        decoded.put(7, 'B');
        decoded.put(26, 'Z');
        decoded.put(1, 'Q');
        String encoded = Cryptogram.encodeMappingToString(decoded);
        String correct = "Q.A...B..................Z";
        assertEquals(correct, encoded);
    }

    @Test
    void decodeMappingFromString() {
        String encoded = "Q.A...B..................Z";
        Map<Integer, Character> decoded = Cryptogram.decodeMappingFromString(encoded);

        assertEquals(4, decoded.size());
        assertEquals('Q', (char)decoded.get(1));
        assertEquals('A', (char)decoded.get(3));
        assertEquals('B', (char)decoded.get(7));
        assertEquals('Z', (char)decoded.get(26));
    }

    @Test
    void encodeMappedQuoteToString() {
        int[] decoded = {6,2,1,8,4,21};
        String encoded = Cryptogram.encodeMappedQuoteToString(decoded);
        assertEquals("6,2,1,8,4,21", encoded);
    }

    @Test
    void decodeMappedQuoteFromString() {
        String encoded = "6,2,1,8,4,21";
        int[] decoded = Cryptogram.decodeMappedQuoteFromString(encoded);
        int[] correct = {6,2,1,8,4,21};
        assertArrayEquals(correct, decoded);
    }

}