import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Game {
    ////////////////////////////////////////
    // State of the current game
    private Cryptogram cryptogram;
    private boolean isLetterMapping;
    private Map<Integer, Character> playerSolution;

    private Player currentPlayer;
    private Players players;

    // Width of the console so that we can split long quotes up onto multiple
    // lines
    private static final int CONSOLE_WIDTH = 50;

    public Game() {
        players = Players.getInstance();
    }

    public void loadPlayer(){
        Scanner scanner = new Scanner(System.in);
        String username;
        System.out.println("Please select your username:");
        username = scanner.nextLine();
        Player player = new Player(username);
        Player p = players.findPlayer(player);
        if(p == null)
        {
            players.addPlayer(player);
            currentPlayer = player;
        }
        else
        {
            currentPlayer = p;
        }
        System.out.println("Player <" + player.getName() + "> loaded.");
    }

    public void playGame(){
        // For now we just start a new game, and assume that number mapping
        // is being used
        isLetterMapping = true;
        generateCryptogram();
    }

    public void generateCryptogram(){
        cryptogram = new Cryptogram(Cryptogram.getRandomQuote());
        playerSolution = new HashMap<Integer, Character>();

        displayCryptogram();
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


    // Show the current state: the encrypted quote, and which letters (if any)
    // have been entered for each bit
    private void displayCryptogram()
    {
        int letterWidth = isLetterMapping ? 2 : 3;
        int lettersThatFit = CONSOLE_WIDTH / letterWidth;
        int nextLineStart = 0;
        int[] mappedQuote = cryptogram.getMappedQuote();

        // Let's be fancy and split the quote up into words
        while (nextLineStart < mappedQuote.length)
        {
            // What can we print on this line?
            int startIndex = nextLineStart;
            int endIndex = nextLineStart + lettersThatFit;

            if (endIndex >= mappedQuote.length)
            {
                // We've reached the end of the quote
                endIndex = mappedQuote.length;
                nextLineStart = endIndex;
            }
            else
            {
                // We're in the middle of the quote
                if (mappedQuote[endIndex] == 0)
                {
                    // This is a space, so continue the next line after it
                    nextLineStart = endIndex + 1;
                }
                else
                {
                    // We are in the middle of a word, so let's find the
                    // previous space (if possible)
                    int spaceSearch = endIndex;
                    while (spaceSearch > startIndex && mappedQuote[spaceSearch] != 0)
                        --spaceSearch;

                    // Did we find one?
                    if (mappedQuote[spaceSearch] == 0)
                    {
                        // Yes, so cut things off here
                        endIndex = spaceSearch;
                        nextLineStart = spaceSearch + 1;
                    }
                    else
                    {
                        // No, so just cut off here, we have no choice
                        nextLineStart = endIndex;
                    }
                }
            }

            displayPieceOfCryptogram(startIndex, endIndex);
        }
    }

    // Display the current state of play for a piece of the cryptogram
    // This is used to display the cryptogram spread across multiple lines
    // for long quotes
    private void displayPieceOfCryptogram(int startIndex, int endIndex)
    {
        StringBuilder mappingLine = new StringBuilder();
        StringBuilder userInputLine = new StringBuilder();

        int[] mappedQuote = cryptogram.getMappedQuote();

        for (int i = startIndex; i < endIndex; i++)
        {
            if (i > startIndex)
            {
                // All characters but the very first get spacing before them
                mappingLine.append(' ');
                userInputLine.append(' ');
            }

            int mapNumber = mappedQuote[i];

            if (mapNumber == 0)
            {
                // Spaces don't get mapped, so we just add empty space
                String spacing = isLetterMapping ? " " : "  ";
                mappingLine.append(spacing);
                userInputLine.append(spacing);
            }
            else
            {
                // Display a letter or number
                if (isLetterMapping)
                {
                    mappingLine.append((char)('A' - 1 + mapNumber));
                }
                else
                {
                    // Add an extra space so things line up
                    userInputLine.append(' ');
                    mappingLine.append(String.format("%2d", mapNumber));
                }

                // Display the user's input
                char userInput = playerSolution.getOrDefault(mapNumber, '_');
                userInputLine.append(userInput);
            }
        }

        System.out.println();
        System.out.println(mappingLine.toString());
        System.out.println(userInputLine.toString());
    }
}
