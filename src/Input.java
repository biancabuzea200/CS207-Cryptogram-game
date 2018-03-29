import java.util.Scanner;

public class Input {
    private static Input instance;

    public static Input getInstance() {
        if (instance == null)
        {
            instance = new Input();
        }
        return instance;
    }


    private Scanner scanner = new Scanner(System.in);
    private boolean isLetterMapping;

    public boolean isLetterMapping() {
        return isLetterMapping;
    }

    public void setLetterMapping(boolean letterMapping) {
        isLetterMapping = letterMapping;
    }


    // Read an input line, handling the Help command if necessary
    public String readLine(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line.toLowerCase().equals("help"))
            {
                HelpMessages.printGameHelp();
            }
            else
            {
                return line;
            }
        }
    }

    public int readCommand()
    {
        String input = scanner.nextLine();
        switch(input) {
            case "help":
                HelpMessages.printGameHelp();
                return 0;
            case "enter letter":
                return 1;
            case "get hint":
                return 2;
            case "undo letter":
                return 3;
            case "save game":
                return 4;
            default:
                System.err.println("Wrong command");
                return 0;
        }
    }

    // Read a number from the input
    public int readNumber(String prompt, int min, int max)
    {
        while (true)
        {
            String line = readLine(prompt);
            try
            {
                int num = Integer.parseInt(line, 10);
                if (num >= min && num <= max)
                    return num;
                else
                    System.out.println("You must choose a number between " + min + " and " + max);

            }
            catch (NumberFormatException ex)
            {
                System.out.println("You must enter a number between " + min + " and " + max);
            }
        }
    }

    // Read a letter from the input
    public char readLetter(String prompt)
    {
        while (true)
        {
            String line = readLine(prompt).toUpperCase();
            if (line.length() == 1)
            {
                char c = line.charAt(0);
                if (c >= 'A' && c <= 'Z')
                    return c;
            }

            // if we got here, the input was not valid
            System.out.println("You must enter a single letter");
        }
    }

    // Read a letter or number for the cryptogram indexing system, depending
    // on what kind of cryptogram is currently being played
    public int readLetterOrNumber(String prompt)
    {
        if (isLetterMapping)
            return (readLetter(prompt) - 'A' + 1);
        else
            return readNumber(prompt, 1, 26);
    }

}
