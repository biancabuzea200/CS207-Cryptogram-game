import java.io.IOException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Game game = new Game();
        Players players = Players.getInstance();
        players.loadPlayers();

        SavedGames.getInstance().loadGames();

        game.loadPlayer();
        Player player = game.getCurrentPlayer();

        //run application for this player
        boolean applicationRunning = true;
        boolean commandSuccess;
        Scanner reader = new Scanner(System.in);
        String line = "";


        while(applicationRunning)
        {
            System.out.println("What do you want to do? Type help and press Enter for a list of commands:");
            System.out.print("<" + player.getName() + ">");
            line = reader.nextLine();
            switch(line.toLowerCase())
            {
                case "play game":
                    game.startNewGame();
                     game.playGame();
                     break;
                 case "load game":
                     commandSuccess = game.loadGame();
                     if (commandSuccess)
                         game.playGame();
                     break;
                case "view scoreboard":
                    players.viewScoreboard();
                    break;
                 case "help":
                     HelpMessages.getInstance().printHelpMessages();
                     break;
                 case "exit":
                     applicationRunning = false;
                     continue;
                 default:
                     System.err.println("Invalid user command.");
            }
        }

        System.out.println("Thank you for playing Cryptograms!");
        players.savePlayers();
    }
}


