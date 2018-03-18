import java.util.*;

public class LetterCryptogram extends Cryptogram {

    public LetterCryptogram() {
        super();
    }

    public char getLetter(char letter) {
        for(Map.Entry entry: letter_mapping.entrySet()){
            if(Character.toUpperCase(letter) == (char)entry.getValue()){
                return (char)entry.getKey();
            }
        }
        return 0;
    }
}
