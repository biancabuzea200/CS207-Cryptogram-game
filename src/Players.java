/**
 * Hristo Yordanov
 */

import java.util.ArrayList;

public class Players {
    private ArrayList<Player> allPlayers;
    private static Players instance;

    private Players()
    {
        allPlayers = new ArrayList<Player>();
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
        // load players from file so we can keep track of players when the application is started more than once
    }

    public static Players getInstance() {
        if (instance == null)
        {
            instance = new Players();
        }
        return instance;
    }

    public void printPlayers()
    {
        for (Player pl : allPlayers)
        {
            System.out.println("player <" + pl.getName() + ">");
        }
    }

    public void savePlayers()
    {

    }
}
