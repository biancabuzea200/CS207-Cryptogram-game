import java.util.ArrayList;

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
