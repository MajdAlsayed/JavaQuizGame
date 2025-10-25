package nl.inholland.quizgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gm;

    @BeforeEach
    void setUp() {
        gm = GameManager.getInstance();
        gm.resetScore();
    }

    @Test
    void shouldIncreaseScoreProperly() {
        gm.increaseScore();
        gm.increaseScore();
        assertEquals(2, gm.getScore(), "Score should increase correctly");
    }

    @Test
    void shouldResetScore() {
        gm.increaseScore();
        gm.resetScore();
        assertEquals(0, gm.getScore(), "Score should reset to zero");
    }

    @Test
    void shouldStorePlayerName() {
        gm.setPlayerName("Majd");
        assertEquals("Majd", gm.getPlayerName(), "Player name should be stored correctly");
    }
}
