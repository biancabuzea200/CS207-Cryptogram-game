import java.util.HashMap;
import java.util.Map;

public class Game {
    private Map<Integer, Character> playerSolution;
    private boolean isLetterMapping;
    private Players players;
    private Player currentPlayer;

    public void loadPlayer(Player player){
        //playerSolution = new HashMap<Integer, Character>();
        currentPlayer = players.findPlayer(player);
        if(currentPlayer != null)
        {
            System.err.println("No player " + player.getName() + " found.");
        }
        else
        {
            System.out.println("Player" + player.getName() + " added.");
        }
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

}
