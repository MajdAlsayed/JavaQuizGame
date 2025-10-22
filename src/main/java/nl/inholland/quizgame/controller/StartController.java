package nl.inholland.quizgame.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nl.inholland.quizgame.data.QuestionRepository;
import nl.inholland.quizgame.model.Answer;
import nl.inholland.quizgame.model.GameManager;
import nl.inholland.quizgame.model.Question;

import java.util.List;


public class StartController {

    @FXML private Label questionLabel;
    @FXML private Label welcomeLabel;
    @FXML private Label scoreLabel;
    @FXML private Label timerLabel;

    @FXML private Button answerButton1;
    @FXML private Button answerButton2;
    @FXML private Button answerButton3;
    @FXML private Button restartButton;

    private List<Question> questions;
    private int index = 0;
    private Timeline timeline;
    private int timeLeft;

    @FXML
    public void initialize() {
        GameManager gm = GameManager.getInstance();

        // Show player name
        welcomeLabel.setText("Welcome, " + gm.getPlayerName() + "!");

        // Bind score label to GameManager’s observable score (Observer Pattern)
        scoreLabel.textProperty().bind(gm.scoreProperty().asString("Score: %d"));

        // Load quiz questions from JSON
        questions = new QuestionRepository().loadQuestions();
        gm.setTotalQuestions(questions.size());
        gm.resetScore();

        if (!questions.isEmpty()) {
            showQuestion();
        } else {
            questionLabel.setText("No questions found! Please load a valid quiz file.");
            disableAllButtons();
        }
    }

    // Show the current question and its answers
    private void showQuestion() {
        Question q = questions.get(index);
        questionLabel.setText(q.getQuestionText());

        // Hide all answer buttons first
        answerButton1.setVisible(false);
        answerButton2.setVisible(false);
        answerButton3.setVisible(false);

        List<Answer> answers = q.getAnswers();

        if (answers.size() > 0) {
            answerButton1.setText(answers.get(0).getText());
            answerButton1.setVisible(true);
        }
        if (answers.size() > 1) {
            answerButton2.setText(answers.get(1).getText());
            answerButton2.setVisible(true);
        }
        if (answers.size() > 2) {
            answerButton3.setText(answers.get(2).getText());
            answerButton3.setVisible(true);
        }

        // Start countdown timer (default 15 seconds if not set)
        startTimer(q.getTimeLimit() > 0 ? q.getTimeLimit() : 15);
    }

    // Starts a countdown timer per question
    private void startTimer(int seconds) {
        // Stop any existing timer
        if (timeline != null) {
            timeline.stop();
        }

        timeLeft = seconds;
        timerLabel.setText("Time left: " + timeLeft);

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeLeft--;
                    timerLabel.setText("Time left: " + timeLeft);

                    // When timer reaches 0 → go to next question automatically
                    if (timeLeft <= 0) {
                        timeline.stop();
                        Platform.runLater(() -> {
                            index++;
                            if (index >= questions.size()) {
                                GameManager.getInstance().goToScene("end-view.fxml");
                            } else {
                                showQuestion();
                            }
                        });
                    }
                })
        );

        timeline.setCycleCount(seconds);
        timeline.play();
    }

    // Handle button clicks
    @FXML private void handleAnswer1() { check(0); }
    @FXML private void handleAnswer2() { check(1); }
    @FXML private void handleAnswer3() { check(2); }

    // Checks if the selected answer is correct and updates score
    private void check(int i) {
        if (timeline != null)
            timeline.stop(); // Stop timer when answered

        GameManager gm = GameManager.getInstance();
        Answer selected = questions.get(index).getAnswers().get(i);

        // Update score
        if (selected.isCorrect()) {
            gm.increaseScore();
        }

        // Move to next question or end
        index++;
        if (index >= questions.size()) {
            gm.goToScene("end-view.fxml");
        } else {
            showQuestion();
        }
    }

    // Disables all buttons (used if quiz fails to load)
    private void disableAllButtons() {
        answerButton1.setDisable(true);
        answerButton2.setDisable(true);
        answerButton3.setDisable(true);
    }

    // Restarts quiz → go back to menu
    @FXML
    private void restartQuiz() {
        if (timeline != null)
            timeline.stop();

        GameManager.getInstance().goToScene("menu-view.fxml");
    }
}
