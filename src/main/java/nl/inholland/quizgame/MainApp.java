package nl.inholland.quizgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.inholland.quizgame.model.GameManager;
import java.io.IOException;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        scene.getStylesheets().add(getClass().getResource("/nl/inholland/quizgame/style.css").toExternalForm());

        stage.setTitle("Java Quiz Game");
        stage.setScene(scene);
        stage.show();

        GameManager.getInstance().setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}