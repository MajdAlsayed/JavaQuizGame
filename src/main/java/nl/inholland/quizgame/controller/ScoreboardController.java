package nl.inholland.quizgame.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import nl.inholland.quizgame.data.ResultRepository;
import nl.inholland.quizgame.model.GameManager;
import nl.inholland.quizgame.model.Result;

import java.util.List;

public class ScoreboardController {

    @FXML private TableView<Result> scoreTable;
    @FXML private TableColumn<Result, String> nameColumn;
    @FXML private TableColumn<Result, Integer> totalColumn;
    @FXML private TableColumn<Result, Integer> correctColumn;
    @FXML private TableColumn<Result, String> dateColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuestions"));
        correctColumn.setCellValueFactory(new PropertyValueFactory<>("correctQuestions"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        List<Result> results = new ResultRepository().loadResults();
        scoreTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleBack() {
        GameManager.getInstance().goToScene("menu-view.fxml");
    }
}
