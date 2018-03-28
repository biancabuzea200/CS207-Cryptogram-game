import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SavedGames {
    private Map<String, Map<String, String>> allGamesByPlayer;
    private static SavedGames instance;

    private SavedGames()
    {
        allGamesByPlayer = new HashMap<>();
    }

    public static SavedGames getInstance() {
        if (instance == null)
        {
            instance = new SavedGames();
        }
        return instance;
    }

    public Set<String> getGamesForPlayer(String playerName)
    {
        if (allGamesByPlayer.containsKey(playerName))
            return allGamesByPlayer.get(playerName).keySet();
        else
            return new HashSet<>();
    }

    public String getGame(String playerName, String name)
    {
        return allGamesByPlayer.get(playerName).get(name);
    }

    public void addGame(String playerName, String name, String gameData)
    {
        if (!allGamesByPlayer.containsKey(playerName))
        {
            allGamesByPlayer.put(playerName, new HashMap<>());
        }

        allGamesByPlayer.get(playerName).put(name, gameData);
    }

    public void removeGame(String playerName, String name)
    {
        allGamesByPlayer.get(playerName).remove(name);
    }

    public void loadGames()
    {
        File gamesFile = new File("games.txt");
        try {
            gamesFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Cannot create file:" + gamesFile.getName());
        }
        int value;
        String[] tokens;
        String line;

        try {
            Scanner scanner = new Scanner(gamesFile);
            while(scanner.hasNextLine())
            {
                line = scanner.nextLine();
                if(line == null)
                {
                    continue;
                }
                tokens = line.split("[\t]");
                addGame(tokens[0], tokens[1], tokens[2]);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File " + gamesFile.getName() + " not found.");
        }


    }


    public void saveGames()
    {
        File gamesFile = new File("games.txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(gamesFile);
        } catch (IOException e) {
            System.err.println("Error opening file " + gamesFile.getName());
        }

        for (Map.Entry<String, Map<String, String>> playerEntry : allGamesByPlayer.entrySet())
        {
            for (Map.Entry<String, String> gameEntry : playerEntry.getValue().entrySet())
            {
                try {
                    fileWriter.write(playerEntry.getKey() + "\t" + gameEntry.getKey() + "\t" + gameEntry.getValue() + "\n");
                    fileWriter.flush();
                } catch (IOException e) {
                    System.err.println("Error flushing the text to " + gamesFile.getName());
                }
            }
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error closing the file: " + gamesFile.getName());
        }
    }
}
