public class Player {
    private String name;
    private int correctGuesses;
    private int totalGuesses;
    private int totalCompletionTime;
    private int numberCryptogramsPlayed;
    private int numberCryptogramsCompleted;

    public Player(String userName)
    {
        name = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateAccuracy(boolean guessIsCorrect)
    {
        if (guessIsCorrect)
            ++correctGuesses;
        ++totalGuesses;
    }

    public void incrementCryptogramsCompleted()
    {
        ++numberCryptogramsCompleted;
    }

    public void updateAverageCompletionTime(int secondsTaken)
    {
        totalCompletionTime += secondsTaken;
    }

    public void incrementCryptogramsPlayed()
    {
        ++numberCryptogramsPlayed;
    }

    public double getAccuracy()
    {
        // returns a value from 0 to 1
        return (double)correctGuesses / (double)totalGuesses;
    }

    public int getNumCryptogramsCompleted()
    {
        return numberCryptogramsCompleted;
    }

    public double getAverageTime()
    {
        return (double)totalCompletionTime / (double)numberCryptogramsCompleted;
    }

    public int getNumCryptogramsPlayed()
    {
        return numberCryptogramsPlayed;
    }


}
