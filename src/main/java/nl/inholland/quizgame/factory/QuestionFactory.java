package nl.inholland.quizgame.factory;

import nl.inholland.quizgame.model.Answer;
import nl.inholland.quizgame.model.Question;

import java.util.List;

public class QuestionFactory {

    // Factory method
    public static Question createQuestion(String type, String text, List<Answer> answers) {
        switch (type.toLowerCase()) {
            case "truefalse":
                // Create true/false style question (only 2 answers)
                return new Question(text, answers.subList(0, 2));
            case "multiple":
            default:
                // Default is multiple-choice question
                return new Question(text, answers);
        }
    }
}
