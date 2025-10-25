package nl.inholland.quizgame.model;

public class Result {
    private String playerName;
    private int totalQuestions;
    private int correctQuestions;
    private String date;

    public String getPlayerName() { return playerName; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectQuestions() { return correctQuestions; }
    public String getDate() { return date; }

    // Constructor for new results (auto add current date)
    public Result(String playerName, int totalQuestions, int correctQuestions) {
        this(playerName, totalQuestions, correctQuestions,
                java.time.LocalDateTime.now().toString());
    }

    // Constructor for reading existing results
    public Result(String playerName, int totalQuestions, int correctQuestions, String date) {
        this.playerName = playerName;
        this.totalQuestions = totalQuestions;
        this.correctQuestions = correctQuestions;
        this.date = date;
    }
}
