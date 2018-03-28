import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PlayerTest {
    @Test
    void updateAccuracy() {
        Player p = new Player("Steve");

        assertEquals(p.getAccuracy(), 0, 0.001);
        p.updateAccuracy(false);
        assertEquals(p.getAccuracy(), 0, 0.001);
        p.updateAccuracy(true);
        assertEquals(p.getAccuracy(), 0.5, 0.001);
        p.updateAccuracy(true);
        assertEquals(p.getAccuracy(), 0.66666, 0.001);
        p.updateAccuracy(false);
        assertEquals(p.getAccuracy(), 0.5, 0.001);
    }

    @Test
    void incrementCryptogramsCompleted() {
        Player p = new Player("Steve");

        assertEquals(p.getNumCryptogramsCompleted(), 0);
        p.incrementCryptogramsCompleted();
        assertEquals(p.getNumCryptogramsCompleted(), 1);
    }

    @Test
    void updateAverageCompletionTime() {
        Player p = new Player("Steve");

        assertEquals(p.getAverageTime(), 0, 0.001);
        p.incrementCryptogramsCompleted();
        p.updateAverageCompletionTime(100);
        assertEquals(p.getAverageTime(), 100, 0.001);
        p.incrementCryptogramsCompleted();
        p.updateAverageCompletionTime(50);
        assertEquals(p.getAverageTime(), 75, 0.001);
        p.incrementCryptogramsCompleted();
        p.updateAverageCompletionTime(0);
        assertEquals(p.getAverageTime(), 50, 0.001);
    }

    @Test
    void incrementCryptogramsPlayed() {
        Player p = new Player("Steve");

        assertEquals(p.getNumCryptogramsPlayed(), 0);
        p.incrementCryptogramsPlayed();
        assertEquals(p.getNumCryptogramsPlayed(), 1);
    }

}