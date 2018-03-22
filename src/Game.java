import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Game {
    private Map<Integer, Character> playerSolution;
    private boolean isLetterMapping;
    private Players players;
    private Player currentPlayer;

    public Game() {
        playerSolution = new HashMap<Integer, Character>();
        players = Players.getInstance();
    }

    public void loadPlayer(){
        Scanner scanner = new Scanner(System.in);
        String username;
        System.out.println("Please select your username:");
        username = scanner.nextLine();
        Player player = new Player(username);
        if(players.findPlayer(player)== null)
        {
            players.addPlayer(player);
        }
        currentPlayer = player;
            System.out.println("Player <" + player.getName() + "> loaded.");

    }

    public void playGame(){

    }

    public void generateCryptogram(){

    }

    public void enterLetter(){

    }

    public void undoLetter(){

    }

    public int viewFrequencies() {
        return 0;
    }

    public void saveGame(){

    }

    public void loadGame(){

    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }
}
