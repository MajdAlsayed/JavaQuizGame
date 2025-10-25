package nl.inholland.quizgame.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import nl.inholland.quizgame.data.ResultRepository;
import nl.inholland.quizgame.model.GameManager;
import nl.inholland.quizgame.model.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class EndController {
    @FXML private Label congratsLabel;
    @FXML private Label scoreLabel;
    @FXML private StackPane feedbackContainer;

    private WebView webView;
    private Label fallbackLabel;

    @FXML
    public void initialize() {
        GameManager gm = GameManager.getInstance();
        String name = gm.getPlayerName();
        int score = gm.getScore();
        int total = gm.getTotalQuestions();

        congratsLabel.setText("Great job, " + name + "!");
        scoreLabel.setText("Your final score: " + score + " / " + total);

        // Save the result
        new ResultRepository().saveResult(new Result(name, total, score));

        // Show feedback based on performance
        showFeedback(score, total);
    }

    private void showFeedback(int score, int total) {
        double percentage = (double) score / total * 100;
        File quizFile = GameManager.getInstance().getQuizFile();
        String htmlContent = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(quizFile);
            JsonNode feedbackNode = root.get("feedback");

            if (feedbackNode != null) {
                if (percentage >= 80 && feedbackNode.has("excellent"))
                    htmlContent = feedbackNode.get("excellent").asText();
                else if (percentage >= 50 && feedbackNode.has("good"))
                    htmlContent = feedbackNode.get("good").asText();
                else if (feedbackNode.has("poor"))
                    htmlContent = feedbackNode.get("poor").asText();
            }
        } catch (IOException e) {
            System.out.println("Error reading feedback from quiz file: " + e.getMessage());
        }

        // Initialize WebView
        webView = new WebView();
        webView.setPrefHeight(150);
        webView.setMaxWidth(600);

        // Fallback Label
        fallbackLabel = new Label();
        fallbackLabel.setWrapText(true);
        fallbackLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

        if (htmlContent != null && !htmlContent.isEmpty()) {
            try {
                webView.getEngine().loadContent(htmlContent);
                feedbackContainer.getChildren().add(webView);
            } catch (Exception ex) {
                System.out.println("WebView error, using fallback label.");
                fallbackLabel.setText(htmlToPlainText(htmlContent));
                feedbackContainer.getChildren().add(fallbackLabel);
            }
        } else {
            fallbackLabel.setText("No feedback available for this score.");
            feedbackContainer.getChildren().add(fallbackLabel);
        }
    }

    private String htmlToPlainText(String html) {
        return html.replaceAll("<[^>]*>", ""); // simple HTML stripper
    }

    @FXML
    private void handlePlayAgain() {
        GameManager.getInstance().resetScore();
        GameManager.getInstance().goToScene("menu-view.fxml");
    }

    @FXML
    private void handleScoreboard() {
        GameManager.getInstance().goToScene("scoreboard-view.fxml");
    }


    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
