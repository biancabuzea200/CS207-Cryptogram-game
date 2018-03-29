import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Cryptogram {

    // Mapping of numbers (1 .. 26) to correct characters
    private Map<Integer, Character> mapping;

    // The original quote containing letters and spaces
    private String originalQuote;

    // The mapped quote containing numbers (1 .. 26) for crypted letters and
    // 0 to represent spaces
    private int[] mappedQuote;

    // Map of numbers (1 .. 26) to frequencies, generated on demand when
    // getFrequencies() is called
    private Map<Integer, Integer> frequencies;


    // Build a Cryptogram using a given quote, and generate a mapping for it
    public Cryptogram(String originalQuote)
    {
        generateMapping(originalQuote);
    }


    // Build a Cryptogram using a previously generated mapping
    // (for use when an old state of play is being loaded)
    public Cryptogram(int[] mappedQuote, Map<Integer, Character> mapping)
    {
        this.mappedQuote = mappedQuote;
        this.mapping = mapping;

        // We don't have the original quote, but we can compute it from
        // the mapped version and the mapping itself
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mappedQuote.length; i++)
        {
            if (mappedQuote[i] == 0)
                builder.append(' ');
            else
                builder.append(mapping.get(mappedQuote[i]));
        }
        originalQuote = builder.toString();
    }


    // Construct a brand new mapping for a given quote
    private void generateMapping(String quote)
    {
        originalQuote = quote.toUpperCase();
        mappedQuote = new int[originalQuote.length()];
        Random random = new Random();

        mapping = new HashMap<>();
        Map<Character, Integer> reverseMapping = new HashMap<>();

        for (int i = 0; i < originalQuote.length(); i++)
        {
            char c = originalQuote.charAt(i);

            if (c == ' ') {
                // Spaces simply map to zero
                mappedQuote[i] = 0;
            } else if (reverseMapping.containsKey(c)) {
                // We've already mapped this character, so just use that
                mappedQuote[i] = reverseMapping.get(c);
            } else {
                // We need to map this character to something!
                int mapTo = selectUnusedMapIndex(random, c);

                // We have an index now, so record it
                mapping.put(mapTo, c);
                reverseMapping.put(c, mapTo);

                // ...and don't forget to add it to the quote array itself
                mappedQuote[i] = mapTo;
            }
        }
    }


    // Randomly pick an unused mapping index that can be used to represent
    // the given character
    private int selectUnusedMapIndex(Random random, char c)
    {
        if (c < 'A' || c > 'Z')
            throw new IllegalArgumentException("Quote contains invalid character (not a letter or a space)");

        // Turn the character into an index from 1 to 26
        int origIndex = (c - 'A' + 1);

        // We want to pick something that:
        //  1) has not already been mapped
        //  2) is not equal to origIndex (i.e. we don't map E -> E)
        int mapTo;
        do
        {
            mapTo = 1 + random.nextInt(26);
        }
        while (mapTo == origIndex || mapping.containsKey(mapTo));

        return mapTo;
    }


    // Load a random quote from the quotes database
    // (TODO: Select one depending on type -- historical or pop culture)
    public static String getRandomQuote(String type)
    {
        String[] quotes = new String[50];
        String line;
        int index = 0;
        File quotesFile = new File("quotes.txt");
        switch(type)
        {
            case "pop":
                quotesFile = new File("pop.txt");
                break;
            case "historical":
                quotesFile = new File("historical.txt");
                break;
            default:
                quotesFile = new File("quotes.txt");
        }
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


    public Map<Integer, Integer> getFrequencies() {
        if (frequencies != null)
            return frequencies;

        frequencies = new HashMap<>();
        for (int i = 0; i < mappedQuote.length; i++)
        {
            int mappedValue = mappedQuote[i];
            if (mappedValue > 0)
            {
                int oldCount = frequencies.getOrDefault(mappedValue, 0);
                int newCount = oldCount + 1;
                frequencies.put(mappedValue, newCount);
            }
        }

        return frequencies;
    }


    public int[] getMappedQuote() {
        return mappedQuote;
    }


    public String getOriginalQuote() {
        return originalQuote;
    }


    public Map<Integer, Character> getMapping() {
        return mapping;
    }


    public static String encodeMappingToString(Map<Integer, Character> mapping)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 1; i <= 26; i++)
        {
            builder.append(mapping.getOrDefault(i, '.'));
        }

        return builder.toString();
    }

    public static Map<Integer, Character> decodeMappingFromString(String str)
    {
        if (str.length() != 26)
            throw new IllegalArgumentException("encoded mapping not the correct length");

        Map<Integer, Character> mapping = new HashMap<>();

        for (int i = 1; i <= 26; i++)
        {
            char c = str.charAt(i - 1);
            if (c != '.')
                mapping.put(i, c);
        }

        return mapping;
    }

    public static String encodeMappedQuoteToString(int[] quote)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < quote.length; i++)
        {
            if (i > 0)
                builder.append(',');
            builder.append(quote[i]);
        }

        return builder.toString();
    }

    public static int[] decodeMappedQuoteFromString(String str)
    {
        String[] pieces = str.split("[,]");
        int[] result = new int[pieces.length];

        for (int i = 0; i < pieces.length; i++)
            result[i] = Integer.parseInt(pieces[i], 10);

        return result;
    }
}
