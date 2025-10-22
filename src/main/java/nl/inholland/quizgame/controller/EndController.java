package nl.inholland.quizgame.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nl.inholland.quizgame.data.ResultRepository;
import nl.inholland.quizgame.model.GameManager;
import nl.inholland.quizgame.model.Result;

/**
 * Handles the final results screen:
 * - Shows final score and player name
 * - Saves result to JSON (for high scores)
 * - Allows restart or exit
 */
public class EndController {

    @FXML private Label congratsLabel;
    @FXML private Label scoreLabel;

    private int score;
    private int total;

    @FXML
    public void initialize() {
        GameManager gm = GameManager.getInstance();
        String name = gm.getPlayerName();
        score = gm.getScore();
        total = gm.getTotalQuestions();

        congratsLabel.setText(" Great job, " + name + "!");
        scoreLabel.setText("Your final score: " + score + " / " + total);

        // Save result to JSON file
        Result result = new Result(name, total, score);
        new ResultRepository().saveResult(result);
    }

    @FXML
    private void handlePlayAgain() {
        GameManager.getInstance().resetScore();
        GameManager.getInstance().goToScene("menu-view.fxml");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
