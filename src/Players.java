/**
 * Hristo Yordanov
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Players {
    private ArrayList<Player> allPlayers;
    private static Players instance;

    private Players()
    {
        allPlayers = new ArrayList<Player>();
    }

    public static Players getInstance() {
        if (instance == null)
        {
            instance = new Players();
        }
        return instance;
    }

    public void addPlayer(Player newPlayer)
    {
        allPlayers.add(newPlayer);
    }

    public void removePlayer(Player playerToBeRemoved)
    {
        for(Player player : allPlayers)
        {
            if(player.getName().equals(playerToBeRemoved)) {
                allPlayers.remove(player);
            }
        }
    }

    public Player findPlayer(Player playerSeekedFor)
    {
        for(Player player : allPlayers)
        {
            if(player.equals(playerSeekedFor)) {
                return player;
            }
        }
        return null;
    }

    public ArrayList<Double> getAllPlayerAccuracies()
    {
        ArrayList<Double> allPlayersAccuracies = new ArrayList<Double>();
        for(Player player : allPlayers)
        {
            allPlayersAccuracies.add(player.getAccuracy());
        }
        return allPlayersAccuracies;
    }

    public ArrayList<Double> getAllPlayersTimes()
    {
        ArrayList<Double> allPlayersTimes = new ArrayList<Double>();
        for(Player player : allPlayers)
        {
            allPlayersTimes.add(player.getAverageTime());
        }
        return allPlayersTimes;
    }

    public ArrayList<Integer> getAllPlayerCryptogramsPlayed()
    {
        ArrayList<Integer> allPlayersCryptogramsPlayed = new ArrayList<Integer>();
        for(Player player : allPlayers)
        {
            allPlayersCryptogramsPlayed.add(player.getNumCryptogramsPlayed());
        }
        return allPlayersCryptogramsPlayed;
    }

    public ArrayList<Integer> getAllPlayersCompletedCryprograms()
    {
        ArrayList<Integer> allPlayersCompletedCryptograms = new ArrayList<Integer>();
        for(Player player : allPlayers)
        {
            allPlayersCompletedCryptograms.add(player.getNumCryptogramsCompleted());
        }
        return allPlayersCompletedCryptograms;
    }

    public void loadPlayers()
    {
        File playersFile = new File("players.txt");
        try {
            playersFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Cannnot create file:" + playersFile.getName());
        }
        String username;
        int value;
        Player player;
        String[] tokens;
        String line;

        try {
            Scanner scanner = new Scanner(playersFile);
            while(scanner.hasNextLine())
            {
                line = scanner.nextLine();
                if(line == null)
                {
                    continue;
                }
                tokens = line.split("[,]");
                username = tokens[0];
                player = new Player(username);
                try
                {
                    value = Integer.parseInt(tokens[1]);
                    player.setCorrectGuesses(value);
                    value = Integer.parseInt(tokens[2]);
                    player.setTotalGuesses(value);
                    value = Integer.parseInt(tokens[3]);
                    player.setTotalCompletionTime(value);
                    value = Integer.parseInt(tokens[4]);
                    player.setNumberCryptogramsPlayed(value);
                    value = Integer.parseInt(tokens[5]);
                    player.setNumberCryptogramsCompleted(value);
                }
                catch(NumberFormatException e)
                {
                    System.err.println("Error parsing numbers from file:" + playersFile.getName());
                }


                allPlayers.add(player);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File " + playersFile.getName() + " not found.");
        }


    }

    public void printPlayers()
    {
        for (Player pl : allPlayers)
        {
            System.out.println("player <" + pl.getName() + "," + pl.getCorrectGuesses() + "," + pl.getTotalGuesses() + "," + pl.getTotalCompletionTime() + "," + pl.getNumCryptogramsPlayed() + "," + pl.getNumCryptogramsCompleted() + ">");
        }
    }

    public void savePlayers()
    {
        File playersFile = new File("players.txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(playersFile);
        } catch (IOException e) {
            System.err.println("Error opening file " + playersFile.getName());
        }
        for (Player player : allPlayers)
        {
            try {
                fileWriter.write(player.getName() + "," + player.getCorrectGuesses() + "," + player.getTotalGuesses() + "," + player.getTotalCompletionTime() + "," + player.getNumCryptogramsPlayed() + "," + player.getNumCryptogramsCompleted());
                fileWriter.write("\n");
                fileWriter.flush();
            } catch (IOException e) {
                System.err.println("Error flushing the text to " + playersFile.getName());
            }
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error closing the file: " + playersFile.getName());
        }
    }
}
