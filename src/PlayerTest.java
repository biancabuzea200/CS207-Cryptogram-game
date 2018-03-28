import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PlayerTest {
    @Test
    void updateAccuracy() {
        Player p = new Player("Steve");

        assertEquals(0, p.getAccuracy(), 0.001);
        p.updateAccuracy(false);
        assertEquals(0, p.getAccuracy(), 0.001);
        p.updateAccuracy(true);
        assertEquals(0.5, p.getAccuracy(), 0.001);
        p.updateAccuracy(true);
        assertEquals(0.66666, p.getAccuracy(), 0.001);
        p.updateAccuracy(false);
        assertEquals(0.5, p.getAccuracy(), 0.001);
    }

    @Test
    void incrementCryptogramsCompleted() {
        Player p = new Player("Steve");

        assertEquals(0, p.getNumCryptogramsCompleted());
        p.incrementCryptogramsCompleted();
        assertEquals(1, p.getNumCryptogramsCompleted());
    }

    @Test
    void updateAverageCompletionTime() {
        Player p = new Player("Steve");

        assertEquals(0, p.getAverageTime(), 0.001);
        p.incrementCryptogramsCompleted();
        p.updateAverageCompletionTime(100);
        assertEquals(100, p.getAverageTime(), 0.001);
        p.incrementCryptogramsCompleted();
        p.updateAverageCompletionTime(50);
        assertEquals(75, p.getAverageTime(), 0.001);
        p.incrementCryptogramsCompleted();
        p.updateAverageCompletionTime(0);
        assertEquals(50, p.getAverageTime(), 0.001);
    }

    @Test
    void incrementCryptogramsPlayed() {
        Player p = new Player("Steve");

        assertEquals(0, p.getNumCryptogramsPlayed());
        p.incrementCryptogramsPlayed();
        assertEquals(1, p.getNumCryptogramsPlayed());
    }

}