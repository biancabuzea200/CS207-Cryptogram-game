import java.util.HashMap;
import java.util.Map;

public class Cryptogram {

    protected Map<Character, Character> letter_mapping;

    public Cryptogram() {
        letter_mapping = new HashMap<Character, Character>();
        letter_mapping.put('A', 'X');
        letter_mapping.put('B', 'W');
        letter_mapping.put('C', 'V');
        letter_mapping.put('D', 'S');
        letter_mapping.put('E', 'T');
        letter_mapping.put('F', 'U');
        letter_mapping.put('G', 'J');
        letter_mapping.put('H', 'D');
        letter_mapping.put('I', 'O');
        letter_mapping.put('J', 'F');
        letter_mapping.put('K', 'R');
        letter_mapping.put('L', 'A');
        letter_mapping.put('M', 'H');
        letter_mapping.put('N', 'M');
        letter_mapping.put('O', 'Z');
        letter_mapping.put('P', 'C');
        letter_mapping.put('Q', 'L');
        letter_mapping.put('R', 'Y');
        letter_mapping.put('S', 'Q');
        letter_mapping.put('T', 'N');
        letter_mapping.put('U', 'K');
        letter_mapping.put('V', 'P');
        letter_mapping.put('W', 'I');
        letter_mapping.put('X', 'B');
        letter_mapping.put('Y', 'G');
        letter_mapping.put('Z', 'E');
    }
}
