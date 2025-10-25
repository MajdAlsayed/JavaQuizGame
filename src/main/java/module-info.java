module nl.inholland.quizgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires javafx.web;

    opens nl.inholland.quizgame to javafx.fxml;
    opens nl.inholland.quizgame.controller to javafx.fxml;

    opens nl.inholland.quizgame.data to com.google.gson, com.fasterxml.jackson.databind, javafx.base;
    opens nl.inholland.quizgame.model to com.google.gson, com.fasterxml.jackson.databind, javafx.base;

    exports nl.inholland.quizgame;
    exports nl.inholland.quizgame.controller;
}
