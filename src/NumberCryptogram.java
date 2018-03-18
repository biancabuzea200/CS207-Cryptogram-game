import java.util.HashMap;
import java.util.Map;

public class NumberCryptogram extends Cryptogram {

    public NumberCryptogram() {
        super();
    }

    public char getLetter(int number) {
        for(Map.Entry entry: letter_mapping.entrySet()){
            if((char)(64 + number) == (char)entry.getValue()){
                return (char)entry.getKey();
            }
        }
        return 0;
    }
}
