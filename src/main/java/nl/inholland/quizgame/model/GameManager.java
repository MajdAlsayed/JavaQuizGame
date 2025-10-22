package nl.inholland.quizgame.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameManager {
    private static GameManager instance;
    private Stage stage;
    private String playerName;
    private File quizFile;
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private int totalQuestions;

    private GameManager() {}


    public void setPlayerName(String name) { this.playerName = name; }
    public String getPlayerName() { return playerName; }

    public void setQuizFile(File file) { this.quizFile = file; }
    public File getQuizFile() { return quizFile; }

    public IntegerProperty scoreProperty() { return score; }
    public int getScore() { return score.get(); }
    public void setScore(int value) { score.set(value); }
    public void increaseScore() { score.set(score.get() + 1); }
    public void resetScore() { score.set(0); }

    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    public int getTotalQuestions() { return totalQuestions; }

    public static GameManager getInstance() {
        if (instance == null)
            instance = new GameManager();
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void goToScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/nl/inholland/quizgame/" + fxmlFile));
            Scene scene = new Scene(loader.load(), 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(" Failed to load scene: " + fxmlFile);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(" Unexpected error while switching scene: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
