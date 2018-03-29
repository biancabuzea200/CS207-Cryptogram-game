import java.util.*;

public class Game {
    ////////////////////////////////////////
    // State of the current game
    private Cryptogram cryptogram;
    private boolean isLetterMapping;
    private Map<Integer, Character> playerSolution;

    private Player currentPlayer;
    private Players players;

    // Timestamp where we started tracking the current play session
    private Date gameStartTimestamp;

    // Seconds recorded for this games in previous play sessions
    // (i.e. if the game was saved and later loaded again)
    private int secondsTaken;

    // Width of the console so that we can split long quotes up onto multiple
    // lines
    private static final int CONSOLE_WIDTH = 50;

    public Game() {
        players = Players.getInstance();
    }

    public void loadPlayer() {
        Scanner scanner = new Scanner(System.in);
        String username;
        System.out.println("Please select your username:");
        username = scanner.nextLine();
        Player player = new Player(username);
        Player p = players.findPlayer(player);
        if (p == null) {
            players.addPlayer(player);
            currentPlayer = player;
        } else {
            currentPlayer = p;
        }
        System.out.println("Player <" + player.getName() + "> loaded.");
    }

    // testing stuff for now

    public void startNewGame()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What kind of cryptogram would you like to solve? Please select a number:");
        System.out.println("1) Number Cryptogram: 5 3 2 9  4 7 1 26 3 4  ...");
        System.out.println("2) Letter Cryptogram: A U W Y  V C J F R V  ...");

        // default to number mapping
        isLetterMapping = false;

        String line = scanner.nextLine();
        if (line == null || line.isEmpty())
        {
            System.out.println("You didn't enter anything, so a number cryptogram will be used.");
        }
        else if (line.equals("1"))
        {
            isLetterMapping = false;
        }
        else if (line.equals("2"))
        {
            isLetterMapping = true;
        }
        else
        {
            System.out.println("You entered an invalid choice, so a number cryptogram will be used.");
        }

        // default to pop culture
        boolean isPop = true;
        System.out.println("What type of quote do you want for your puzzle? Please select a number:");
        System.out.println("1) A Pop Culture Quote");
        System.out.println("2) A Historical Quote");

        line = scanner.nextLine();
        if (line == null || line.isEmpty())
        {
            System.out.println("You didn't enter anything, so you will get a pop culture quote.");
        }
        else if (line.equals("1"))
        {
            isPop = true;
        }
        else if (line.equals("2"))
        {
            isPop = false;
        }
        else
        {
            System.out.println("You entered an invalid choice, so you will get a pop culture quote.");
        }

        // TODO: we need to use the different quote types
        generateCryptogram();
    }

    public void playGame() {
        gameStartTimestamp = new Date();

        while(true)
        {
            switch(enterLetter())
            {
                case 0:
                    break;
                case -1:
                    continue;
                case -2:
                    isLetterMapping = true;
                    generateCryptogram();

                    secondsTaken = 0;
                    gameStartTimestamp = new Date();

                    continue;
                case -3:
                    return;
            }
            displayCryptogram();
            getHint();
            displayCryptogram();
            switch(undoLetter())
            {
                case 0:
                    break;
                case -1:
                    continue;
                case -2:
                    isLetterMapping = true;
                    generateCryptogram();

                    secondsTaken = 0;
                    gameStartTimestamp = new Date();

                    continue;
                case -3:
                    return;
            }
            displayCryptogram();
            if(isLastLetter())
            {
                checkSolution();
                return;
            }
            saveGame();
        }
    }

    public void generateCryptogram() {
        cryptogram = new Cryptogram(Cryptogram.getRandomQuote());
        playerSolution = new HashMap<Integer, Character>();
        secondsTaken = 0;

        currentPlayer.incrementCryptogramsPlayed();
        displayCryptogram();
    }

    private int printHelp()
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

    public int enterLetter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select a letter from the Cryptogram to map a value to it.");
        System.out.print("Letter:");
        String line = scanner.nextLine().toUpperCase();
        if(line.equals("HELP"))
        {
            return printHelp();
        }
        if(line == null)
        {
            return -1;
        }
        char keyChar = line.charAt(0);
        if(line.length() != 1)
        {
            System.err.println("Incorrect letter!");
        }
        int keyInteger = (keyChar - 'A' + 1);
        System.out.println("What enter would you like to enter/change it to?");
        System.out.print("Letter:");
        line = scanner.nextLine().toUpperCase();
        if(line == null)
        {
            return -1;
        }
        char value = line.charAt(0);
        if(line.length() != 1)
        {
            System.err.println("Incorrect letter!");
        }

        playerSolution.put(keyInteger, value);
        return 0;
    }

    public int undoLetter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select a letter from the Cryptogram to remove its mapped value.");
        System.out.print("Clear Letter:");
        String line = scanner.nextLine().toUpperCase();
        if(line.equals("HELP"))
        {
            return printHelp();
        }
        if(line == null)
        {
            return -1;
        }
        char keyChar = line.charAt(0);
        if(line.length() != 1)
        {
            System.err.println("Incorrect letter!");
        }
        int keyInteger = (keyChar - 'A' + 1);
        playerSolution.remove(keyInteger);
        return 0;
    }

    public int viewFrequencies() {
        return 0;
    }

    // Turn the current game state into a string that we can store
    public String saveGameToString()
    {
        updateSecondsTaken();

        StringBuilder builder = new StringBuilder();
        builder.append(Cryptogram.encodeMappedQuoteToString(cryptogram.getMappedQuote()));
        builder.append('|');
        builder.append(Cryptogram.encodeMappingToString(cryptogram.getMapping()));
        builder.append('|');
        builder.append(Cryptogram.encodeMappingToString(playerSolution));
        builder.append('|');
        builder.append(secondsTaken);
        builder.append('|');
        builder.append(isLetterMapping ? 'L' : 'N');

        return builder.toString();
    }

    // Replace the current game state with one loaded from a string
    public void loadGameFromString(String str)
    {
        String[] pieces = str.split("[|]");

        if (pieces.length != 5)
            throw new IllegalArgumentException("saved game format invalid");

        int[] mappedQuote = Cryptogram.decodeMappedQuoteFromString(pieces[0]);
        Map<Integer, Character> mapping = Cryptogram.decodeMappingFromString(pieces[1]);
        cryptogram = new Cryptogram(mappedQuote, mapping);

        playerSolution = Cryptogram.decodeMappingFromString(pieces[2]);
        secondsTaken = Integer.parseInt(pieces[3], 10);
        isLetterMapping = (pieces[4].equals("L"));

        gameStartTimestamp = new Date();
    }

    public void saveGame() {
        System.out.println("Please enter a name for this saved game:");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if(line == null || line.isEmpty())
        {
            System.out.println("Game not saved!");
            return;
        }

        String gameData = saveGameToString();
        SavedGames.getInstance().addGame(currentPlayer.getName(), line, gameData);
        SavedGames.getInstance().saveGames();
        System.out.println("Your game has been saved!");

        // TODO: Should we quit the game back to the main menu after this happens?
    }

    public boolean loadGame() {
        Set<String> gameNameSet = SavedGames.getInstance().getGamesForPlayer(currentPlayer.getName());
        String[] gameNames = gameNameSet.toArray(new String[0]);

        if (gameNames.length == 0)
        {
            System.out.println("You have no saved games to load!");
            return false;
        }

        System.out.println("Which game would you like to load? Type in the number:");
        for (int i = 0; i < gameNames.length; i++)
        {
            System.out.print(i + 1);
            System.out.println(") " + gameNames[i]);
        }

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if(line == null || line.isEmpty())
        {
            System.out.println("You must enter a number!");
            return false;
        }
        int gameNum;
        try
        {
            gameNum = Integer.parseInt(line, 10);
        }
        catch (NumberFormatException ex)
        {
            System.out.println("You must enter a number!");
            return false;
        }

        if(gameNum <= 0 || gameNum > gameNames.length)
        {
            System.out.println("You must enter a number between 1 and " + gameNames.length + "!");
            return false;
        }

        String gameName = gameNames[gameNum - 1];
        String gameData = SavedGames.getInstance().getGame(currentPlayer.getName(), gameName);
        System.out.println("OK, loading game " + gameName + "...");
        loadGameFromString(gameData);
        System.out.println("Done!");
        displayCryptogram();
        return true;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    private void updateSecondsTaken()
    {
        long millisecondDelta = (new Date().getTime()) - gameStartTimestamp.getTime();
        secondsTaken += (millisecondDelta / 1000);

        // Reset the timestamp to now so that we won't re-record seconds we've
        // already tracked
        gameStartTimestamp = new Date();
    }


    // Show the current state: the encrypted quote, and which letters (if any)
    // have been entered for each bit
    private void displayCryptogram() {
        int letterWidth = isLetterMapping ? 2 : 3;
        int lettersThatFit = CONSOLE_WIDTH / letterWidth;
        int nextLineStart = 0;
        int[] mappedQuote = cryptogram.getMappedQuote();

        // Let's be fancy and split the quote up into words
        while (nextLineStart < mappedQuote.length) {
            // What can we print on this line?
            int startIndex = nextLineStart;
            int endIndex = nextLineStart + lettersThatFit;

            if (endIndex >= mappedQuote.length) {
                // We've reached the end of the quote
                endIndex = mappedQuote.length;
                nextLineStart = endIndex;
            } else {
                // We're in the middle of the quote
                if (mappedQuote[endIndex] == 0) {
                    // This is a space, so continue the next line after it
                    nextLineStart = endIndex + 1;
                } else {
                    // We are in the middle of a word, so let's find the
                    // previous space (if possible)
                    int spaceSearch = endIndex;
                    while (spaceSearch > startIndex && mappedQuote[spaceSearch] != 0)
                        --spaceSearch;

                    // Did we find one?
                    if (mappedQuote[spaceSearch] == 0) {
                        // Yes, so cut things off here
                        endIndex = spaceSearch;
                        nextLineStart = spaceSearch + 1;
                    } else {
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
    private void displayPieceOfCryptogram(int startIndex, int endIndex) {
        StringBuilder mappingLine = new StringBuilder();
        StringBuilder userInputLine = new StringBuilder();

        int[] mappedQuote = cryptogram.getMappedQuote();

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                // All characters but the very first get spacing before them
                mappingLine.append(' ');
                userInputLine.append(' ');
            }

            int mapNumber = mappedQuote[i];

            if (mapNumber == 0) {
                // Spaces don't get mapped, so we just add empty space
                String spacing = isLetterMapping ? " " : "  ";
                mappingLine.append(spacing);
                userInputLine.append(spacing);
            } else {
                // Display a letter or number
                if (isLetterMapping) {
                    mappingLine.append((char) ('A' - 1 + mapNumber));
                } else {
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

    private boolean checkSolution()
    {
        if (playerSolution.equals(cryptogram.getMapping()))
        {
            // Game completed
            updateSecondsTaken();
            System.out.println();
            System.out.println("* * * CONGRATULATIONS * * *");
            System.out.println("You have solved this cryptogram in " + secondsTaken + " seconds!");
            currentPlayer.updateAccuracy(true);
            currentPlayer.updateAverageCompletionTime(secondsTaken);
            currentPlayer.incrementCryptogramsCompleted();
            return true;
        }
        else
        {
            // Solution incorrect
            System.out.println();
            System.out.println("* * * TRY AGAIN * * *");
            System.out.println("Your solution is incorrect. Please try again.");
            currentPlayer.updateAccuracy(false);
            return false;
        }
    }

    private void showSolution()
    {
        HashMap<Integer, Character> solution = (HashMap<Integer,Character>) cryptogram.getMapping();
        Map<Integer, Character> temp = playerSolution;
        playerSolution = solution;
        System.out.println("* * * SHOWING SOLUTION * * *");
        displayCryptogram();
        playerSolution = temp;
    }

    // Checks if player's solution is same size as mapping.
    private boolean isLastLetter()
    {
        return playerSolution.keySet().size() == cryptogram.getMapping().size();
    }

    // gets a hint (fills one letter in the solution that a player hasn't mapped a value to
    private void getHint()
    {
        for(char charValue : cryptogram.getMapping().values())
        {
            if(!playerSolution.containsKey(charValue - 'A' + 1))
            {
                for(int letterInteger : cryptogram.getMapping().keySet())
                {
                    if(cryptogram.getMapping().get(letterInteger) == charValue)
                    {
                        char letterChar = (char)(letterInteger + 'A' - 1);
                        int charInt = charValue - 'A' + 1;
                        playerSolution.put(charInt, letterChar);
                        System.out.println("Hint: " + letterChar + " is mapped to " + charValue + ".");
                        return;
                    }
                }
            }
        }
    }

}
