import java.util.ArrayList;
import java.util.Scanner;

public class HelpMessages {
    private ArrayList<String> helpMessages;
    private static HelpMessages instance;

    private HelpMessages()
    {
        helpMessages = new ArrayList<String>();
        helpMessages.add("=====HELP=====");
        helpMessages.add("\"play game\" - allows you to start a new game");
        helpMessages.add("\"load game\" - allows you to load a previously saved game");
        helpMessages.add("\"view scoreboard\" - allows you to view the players scoreboard");
        helpMessages.add("\"exit\" - exits the application");

    }

    public static int printGameHelp()
    {
        System.out.println("======= HELP =======");
        System.out.println("\"continue\" - continue the game");
        System.out.println("\"new cryptogram\" - discard the current cryptogram and generate a new one");
        System.out.println("\"exit\" - exit the game and return to main page");

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().toLowerCase();
        switch (line) {
            case "continue":
                return -1;
            case "new cryptogram":
                return -2;
            case "exit":
                return -3;
            default:
                System.out.println("Wrong command");
                return 0;
        }
    }

    public static HelpMessages getInstance()
    {
        if(instance == null)
        {
            instance = new HelpMessages();
        }
        return instance;
    }

    public void printHelpMessages()
    {
        for(String message : helpMessages)
        {
            System.out.println(message);
        }
    }
}
