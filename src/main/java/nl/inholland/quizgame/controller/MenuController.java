package nl.inholland.quizgame.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import nl.inholland.quizgame.model.GameManager;

import java.io.File;

public class MenuController {
    @FXML private TextField nameField;
    @FXML private Label statusLabel;
    @FXML private Button startButton;
    @FXML private Button loadQuizButton;

    @FXML
    private void handleLoadQuiz() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Quiz JSON File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

            File file = fileChooser.showOpenDialog(null);
            if (file == null) {
                statusLabel.setText(" No file selected.");
                return;
            }

            if (!file.getName().endsWith(".json")) {
                statusLabel.setText(" Please choose a valid JSON file.");
                return;
            }

            GameManager.getInstance().setQuizFile(file);
            statusLabel.setText(" Quiz loaded successfully!");
            startButton.setDisable(false);

        } catch (Exception e) {
            statusLabel.setText(" Error loading quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStartQuiz() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setText("⚠ Please enter your name before starting.");
            return;
        }

        try {
            GameManager gm = GameManager.getInstance();
            gm.setPlayerName(name);
            gm.goToScene("start-view.fxml");
        } catch (Exception e) {
            statusLabel.setText(" Failed to start quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
