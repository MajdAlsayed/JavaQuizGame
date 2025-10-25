package nl.inholland.quizgame.data;

import nl.inholland.quizgame.model.GameManager;
import nl.inholland.quizgame.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionRepositoryTest {
    private QuestionRepository repo;

    @BeforeEach
    void setUp() {
        repo = new QuestionRepository();

        // Load test quiz file
        File testFile = new File("src/test/resources/test-questions.json");
        GameManager.getInstance().setQuizFile(testFile);
    }

    @Test
    void shouldLoadQuestionsFromValidJson() {
        List<Question> questions = repo.loadQuestions();
        assertNotNull(questions, "Questions list should not be null");
        assertFalse(questions.isEmpty(), "Questions list should not be empty");
        assertEquals("What is the capital of France?", questions.get(0).getQuestionText());
    }

    @Test
    void shouldReturnEmptyListForInvalidJson() {
        // Load invalid file
        GameManager.getInstance().setQuizFile(new File("src/test/resources/invalid.json"));
        List<Question> questions = repo.loadQuestions();
        assertTrue(questions.isEmpty(), "Should return empty list when JSON invalid");
    }
}
