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
    private static final int CONSOLE_WIDTH = 80;

    // Handler for user input
    private Input input;

    public Game() {
        players = Players.getInstance();
        input = Input.getInstance();
    }



    public void loadPlayer() {
        String username = input.readLine("Please select your username:");
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

    public void startNewGame()
    {
        System.out.println("What kind of cryptogram would you like to solve? Please select a number:");
        System.out.println("1) Number Cryptogram: 5 3 2 9  4 7 1 26 3 4  ...");
        System.out.println("2) Letter Cryptogram: A U W Y  V C J F R V  ...");

        int mappingChoice = input.readNumber("Choice:", 1, 2);
        isLetterMapping = (mappingChoice == 2);
        input.setLetterMapping(isLetterMapping);

        System.out.println("What type of quote do you want for your puzzle? Please select a number:");
        System.out.println("1) A Pop Culture Quote");
        System.out.println("2) A Historical Quote");

        int quoteChoice = input.readNumber("Choice:", 1, 2);
        boolean isPop = (quoteChoice == 1);

        // TODO: we need to use the different quote types
        generateCryptogram();
    }

    public void playGame() {
        gameStartTimestamp = new Date();

        while(true)
        {
            System.out.println("Enter your command, or type help for a list");
            switch(input.readCommand()) {
                case 0:
                    continue;
                case 1:
                    enterLetter();
                    break;
                case 2:
                    getHint();
                    break;
                case 3:
                    undoLetter();
                    break;
                case 4:
                    saveGame();
                    break;
                case 5:
                    return;
                case 6:
                    viewFrequencies();
                    break;
            }
            displayCryptogram();
            if(isLastLetter()) {
                if (checkSolution())
                    return;
            }
        }
    }

    public void generateCryptogram() {
        cryptogram = new Cryptogram(Cryptogram.getRandomQuote());
        playerSolution = new HashMap<>();
        secondsTaken = 0;

        currentPlayer.incrementCryptogramsPlayed();
        displayCryptogram();
    }

    private int checkIfMappingUsed(char value)
    {
        for (Map.Entry<Integer, Character> e : playerSolution.entrySet())
        {
            if (e.getValue() == value)
                return e.getKey();
        }

        return 0;
    }

    public void enterLetter() {
        System.out.println("Please select a letter from the Cryptogram to map a value to it.");
        int keyInteger = input.readLetterOrNumber("Letter:");

        System.out.println("What letter would you like to enter/change it to?");
        char value = input.readLetter("Letter:");

        int alreadyUsed = checkIfMappingUsed(value);
        if (alreadyUsed > 0)
        {
            System.out.println("You have already used this letter in your solution! What do you want to do?");
            System.out.println("1) Pick something different to enter");
            System.out.println("2) Remove this letter from your solution");
            int choice = input.readNumber("Choice:", 1, 2);
            if (choice == 1)
                enterLetter();
            else {
                playerSolution.remove(alreadyUsed);
                playerSolution.put(keyInteger, value);
            }
        }
        else
        {
            playerSolution.put(keyInteger, value);
        }
    }

    public void undoLetter() {
        System.out.println("Please select a letter from the Cryptogram to remove its mapped value.");
        int keyInteger = input.readLetterOrNumber("Clear Letter:");

        if (playerSolution.containsKey(keyInteger))
            playerSolution.remove(keyInteger);
        else
            System.out.println("You didn't have anything mapped to that!");
    }

    public void viewFrequencies() {
        HashMap<Integer, Integer> frequencies = (HashMap<Integer, Integer>) cryptogram.getFrequencies();
        for(int key : frequencies.keySet()) {
            if(isLetterMapping)
            {
                char charValue = (char)(key + 'A' - 1);
                System.out.print("[" + charValue + "] = " + frequencies.get(key));
            }
            else {
                System.out.print("[" + key + "] = " + frequencies.get(key));
            }
        }
        System.out.println();
    }

    // Turn the current game state into a string that we can store
    public String saveGameToString(boolean includeStateOfPlay)
    {
        updateSecondsTaken();

        StringBuilder builder = new StringBuilder();
        builder.append(Cryptogram.encodeMappedQuoteToString(cryptogram.getMappedQuote()));
        builder.append('|');
        builder.append(Cryptogram.encodeMappingToString(cryptogram.getMapping()));
        builder.append('|');
        if (includeStateOfPlay) {
            builder.append(Cryptogram.encodeMappingToString(playerSolution));
            builder.append('|');
            builder.append(secondsTaken);
        } else {
            // just include an empty state
            // no mappings, no time taken
            builder.append(Cryptogram.encodeMappingToString(new HashMap<>()));
            builder.append("|0");
        }
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
        input.setLetterMapping(isLetterMapping);

        gameStartTimestamp = new Date();
    }

    public void saveGame() {
        String gameName = input.readLine("Please enter a name for this saved game:");

        System.out.println("Would you like to save the state of play?");
        System.out.println("1) No, just save the puzzle");
        System.out.println("2) Yes, include the progress as well");
        int choice = input.readNumber("Choice:", 1, 2);
        boolean includeStateOfPlay = (choice == 2);

        String gameData = saveGameToString(includeStateOfPlay);
        SavedGames.getInstance().addGame(currentPlayer.getName(), gameName, gameData);
        SavedGames.getInstance().saveGames();
        System.out.println("Your game has been saved!");
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

        int gameNum = input.readNumber("Game:", 1, gameNames.length);
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
        // figure out which possibilities we can hint
        Collection<Integer> usedKeys = playerSolution.keySet();
        Collection<Character> usedValues = playerSolution.values();
        ArrayList<Map.Entry<Integer, Character>> possibilities = new ArrayList<>();

        for (Map.Entry<Integer, Character> e : cryptogram.getMapping().entrySet())
        {
            if (!usedKeys.contains(e.getKey()) && !usedValues.contains(e.getValue()))
                possibilities.add(e);
        }

        if (possibilities.isEmpty())
        {
            System.out.println("There are no possible hints left, you must remove an incorrect mapping first!");
        }
        else
        {
            int index = new Random().nextInt(possibilities.size());
            Map.Entry<Integer, Character> e = possibilities.get(index);

            playerSolution.put(e.getKey(), e.getValue());

            System.out.print("Hint: ");
            if (isLetterMapping)
                System.out.print((char)(e.getKey() + 'A' - 1));
            else
                System.out.print(e.getKey());
            System.out.print(" is mapped to ");
            System.out.print(e.getValue());
            System.out.println(".");
        }
    }

}
