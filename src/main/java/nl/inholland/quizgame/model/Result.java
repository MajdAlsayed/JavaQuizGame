package nl.inholland.quizgame.model;

import java.time.LocalDateTime;

public class Result {
    private String playerName;
    private int totalQuestions;
    private int correctQuestions;
    private LocalDateTime date;

    public String getPlayerName() { return playerName; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectQuestions() { return correctQuestions; }
    public LocalDateTime getDate() { return date; }


    public Result(String playerName, int totalQuestions, int correctQuestions) {
        this.playerName = playerName;
        this.totalQuestions = totalQuestions;
        this.correctQuestions = correctQuestions;
        this.date = LocalDateTime.now();
    }
}
