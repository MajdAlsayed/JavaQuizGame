package nl.inholland.quizgame.model;

import java.util.List;

public class Question {
    private String questionText;
    private List<Answer> answers;
    private int timeLimit;

    public Question() {
        this.timeLimit = 15;
    }

    public Question(String questionText, List<Answer> answers) {
        this.questionText = questionText;
        this.answers = answers;
        this.timeLimit = 15;
    }

    public Question(String questionText, List<Answer> answers, int timeLimit) {
        this.questionText = questionText;
        this.answers = answers;
        this.timeLimit = timeLimit;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
}
