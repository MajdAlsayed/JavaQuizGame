package nl.inholland.quizgame.controller;

import javafx.animation.FadeTransition;
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
    @FXML private Button confirmButton;
    @FXML private Button restartButton;

    private List<Question> questions;
    private int index = 0;
    private int selectedAnswerIndex = -1;
    private Timeline timeline;
    private int timeLeft;

    @FXML
    public void initialize() {
        GameManager gm = GameManager.getInstance();

        welcomeLabel.setText("Welcome, " + gm.getPlayerName() + "!");
        scoreLabel.textProperty().bind(gm.scoreProperty().asString("Score: %d"));

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

    private void showQuestion() {
        Question q = questions.get(index);
        questionLabel.setText(q.getQuestionText());

        selectedAnswerIndex = -1;
        confirmButton.setVisible(false);
        resetButtonColors();

        List<Answer> answers = q.getAnswers();
        setButton(answerButton1, answers, 0);
        setButton(answerButton2, answers, 1);
        setButton(answerButton3, answers, 2);

        animateQuestion();
        startTimer(q.getTimeLimit() > 0 ? q.getTimeLimit() : 15);
    }

    private void setButton(Button btn, List<Answer> answers, int i) {
        if (answers.size() > i) {
            btn.setText(answers.get(i).getText());
            btn.setVisible(true);
            btn.setDisable(false);
        } else {
            btn.setVisible(false);
        }
    }

    @FXML private void selectAnswer1() { selectAnswer(0); }
    @FXML private void selectAnswer2() { selectAnswer(1); }
    @FXML private void selectAnswer3() { selectAnswer(2); }

    private void selectAnswer(int i) {
        selectedAnswerIndex = i;
        confirmButton.setVisible(true);
        highlightSelected(i);
    }

    private void highlightSelected(int selected) {
        resetButtonColors();
        Button[] buttons = {answerButton1, answerButton2, answerButton3};
        if (selected >= 0 && selected < buttons.length) {
            buttons[selected].getStyleClass().add("answer-selected");
        }
    }

    private void resetButtonColors() {
        Button[] buttons = {answerButton1, answerButton2, answerButton3};
        for (Button btn : buttons) {
            btn.getStyleClass().remove("answer-selected");
            btn.getStyleClass().remove("correct");
            btn.getStyleClass().remove("wrong");
        }
    }

    @FXML
    private void handleConfirm() {
        if (selectedAnswerIndex == -1) return;
        if (timeline != null) timeline.stop();

        GameManager gm = GameManager.getInstance();
        Question currentQuestion = questions.get(index);
        Answer selected = currentQuestion.getAnswers().get(selectedAnswerIndex);
        Button selectedButton = getSelectedButton();

        boolean isCorrect = selected.isCorrect();

        if (selected.isCorrect()) {
            gm.increaseScore();
        }


        showFeedbackAnimation(selectedButton, isCorrect);
    }

    private void showFeedbackAnimation(Button selectedButton, boolean isCorrect) {
        String cssClass = isCorrect ? "correct" : "wrong";
        selectedButton.getStyleClass().add(cssClass);

        new Thread(() -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                selectedButton.getStyleClass().remove(cssClass);
                index++;
                if (index >= questions.size()) {
                    GameManager.getInstance().goToScene("end-view.fxml");
                } else {
                    showQuestion();
                }
            });
        }).start();
    }

    private Button getSelectedButton() {
        return switch (selectedAnswerIndex) {
            case 0 -> answerButton1;
            case 1 -> answerButton2;
            case 2 -> answerButton3;
            default -> null;
        };
    }

    private void startTimer(int seconds) {
        if (timeline != null) timeline.stop();

        timeLeft = seconds;
        timerLabel.setText("Time left: " + timeLeft);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft);

            if (timeLeft <= 0) {
                timeline.stop();
                Platform.runLater(() -> {
                    GameManager gm = GameManager.getInstance();

                    if (selectedAnswerIndex == -1) {
                        index++;
                        if (index >= questions.size()) {
                            gm.goToScene("end-view.fxml");
                        } else {
                            showQuestion();
                        }
                    } else {
                        handleConfirm();
                    }
                });
            }
        }));

        timeline.setCycleCount(seconds);
        timeline.play();
    }

    private void animateQuestion() {
        FadeTransition ft = new FadeTransition(Duration.millis(400), questionLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void disableAllButtons() {
        answerButton1.setDisable(true);
        answerButton2.setDisable(true);
        answerButton3.setDisable(true);
    }

    @FXML
    private void restartQuiz() {
        if (timeline != null) timeline.stop();
        GameManager.getInstance().goToScene("menu-view.fxml");
    }
}
