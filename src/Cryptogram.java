import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Cryptogram {

    protected Map<Character, Character> letter_mapping;

    public Cryptogram() {
        letter_mapping = new HashMap<Character, Character>();
    }

    public void generateCryptogram(String type)
    {
        //String quote = getQuote();
        if(type.toLowerCase().equals("letter"))
        {

        }
        else if (type.toLowerCase().equals("number"))
        {

        }


    }

    public String getQuote()
    {
        String[] quotes = new String[50];
        String line;
        int index = 0;
        File quotesFile = new File("quotes.txt");
        try
        {
            Scanner scanner = new Scanner(quotesFile);
            while(scanner.hasNextLine())
            {
                line = scanner.nextLine();
                if(line == null)
                {
                    continue;
                }
                quotes[index] = line;
                index++;
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error opening the file: " + quotesFile.getName());
        }

        Random random = new Random();
        int randomIndex = random.nextInt(index);
        return quotes[randomIndex];
    }
}
