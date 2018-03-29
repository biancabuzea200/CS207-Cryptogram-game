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

    public static void printGameHelp()
    {
        System.out.println("======= HELP =======");
        System.out.println("\"enter letter\" - insert a new letter");
        System.out.println("\"undo letter\" - clear a letter");
        System.out.println("\"get hint\" - reveals a letter");
        System.out.println("\"save game\" - saves your progress");
        System.out.println("\"exit\" - exits the current game and returns to the main menu");
        System.out.println("\"help\" - get the help list");
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
