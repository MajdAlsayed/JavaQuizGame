package nl.inholland.quizgame.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.inholland.quizgame.data.UserRepository;
import nl.inholland.quizgame.model.GameManager;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserRepository userRepo = new UserRepository();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            errorLabel.setText("Please enter both username and password.");
            errorLabel.setVisible(true);
            return;
        }

        if (userRepo.validateUser(username, password)) {
            GameManager.getInstance().setPlayerName(username);
            GameManager.getInstance().goToScene("menu-view.fxml");
        } else {
            errorLabel.setText("Invalid username or password!");
            errorLabel.setVisible(true);
        }
    }
}
