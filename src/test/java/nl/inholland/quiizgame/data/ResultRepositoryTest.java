package nl.inholland.quizgame.data;

import nl.inholland.quizgame.model.Result;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultRepositoryTest {

    private ResultRepository repo;
    private File resultFile;

    @BeforeEach
    void setUp() {
        repo = new ResultRepository();
        resultFile = new File("results/invalid-results.json"); // <-- changed this line
        if (resultFile.exists()) {
            resultFile.delete();
        }
    }


    @Test
    void shouldSaveResultToFile() {
        Result result = new Result("Majd", 3, 5);
        repo.saveResult(result);

        assertTrue(resultFile.exists(), "Result file should be created after saving");
        assertTrue(resultFile.length() > 0, "Result file should not be empty");
    }

    @Test
    void shouldLoadSavedResults() {
        Result result = new Result("Omar", 4, 5);
        repo.saveResult(result);

        List<Result> results = repo.loadResults();

        assertNotNull(results, "Results list should not be null");
        assertFalse(results.isEmpty(), "Results list should contain at least one item");
        assertEquals("Omar", results.get(0).getPlayerName(), "Saved player name should match");
    }

    @AfterEach
    void tearDown() {
        // Clean up file after test
        if (resultFile.exists()) {
            resultFile.delete();
        }
    }
}
