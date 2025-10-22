package nl.inholland.quizgame.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.inholland.quizgame.factory.QuestionFactory;
import nl.inholland.quizgame.model.Answer;
import nl.inholland.quizgame.model.GameManager;
import nl.inholland.quizgame.model.Question;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    public List<Question> loadQuestions() {
        File file = GameManager.getInstance().getQuizFile();
        if (file == null) {
            System.out.println(" No quiz file selected.");
            return new ArrayList<>();
        }

        List<Question> questions = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(file);

            // Check if file loaded
            if (root == null || root.isEmpty()) {
                System.out.println(" Empty or unreadable JSON file.");
                return new ArrayList<>();
            }

            // Try to get pages, or assume root contains elements directly
            JsonNode pages = root.get("pages");
            if (pages == null || !pages.isArray()) {
                System.out.println(" 'pages' array not found, checking root for 'elements'...");

                // Fallback: maybe file directly has elements
                JsonNode elements = root.get("elements");
                if (elements != null && elements.isArray()) {
                    parseElements(elements, questions, 15); // default 15 seconds per question
                } else {
                    System.out.println(" No questions found in file.");
                }

                return questions;
            }

            // Normal case — file has pages array
            for (JsonNode page : pages) {
                int timeLimit = page.has("timeLimit") ? page.get("timeLimit").asInt() : 15;
                JsonNode elements = page.get("elements");
                if (elements != null && elements.isArray()) {
                    parseElements(elements, questions, timeLimit);
                }
            }

        } catch (IOException e) {
            System.out.println(" Error reading quiz JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println(" Unexpected error while loading quiz: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }

        System.out.println(" Loaded " + questions.size() + " questions successfully.");
        return questions;
    }

    private void parseElements(JsonNode elements, List<Question> questions, int timeLimit) {
        for (JsonNode element : elements) {
            try {
                String type = element.has("type") ? element.get("type").asText() : "radiogroup";
                String title = element.has("title") ? element.get("title").asText() : "Untitled Question";

                List<Answer> answers = new ArrayList<>();

                if (type.equalsIgnoreCase("radiogroup")) {
                    String correctAnswer = element.has("correctAnswer") ? element.get("correctAnswer").asText() : "";
                    JsonNode choices = element.get("choices");
                    if (choices != null && choices.isArray()) {
                        for (JsonNode choice : choices) {
                            String choiceText = choice.asText();
                            boolean isCorrect = choiceText.equalsIgnoreCase(correctAnswer);
                            answers.add(new Answer(choiceText, isCorrect));
                        }
                    }
                } else if (type.equalsIgnoreCase("boolean")) {
                    boolean correct = element.has("correctAnswer") && element.get("correctAnswer").asBoolean();
                    String trueLabel = element.has("labelTrue") ? element.get("labelTrue").asText() : "True";
                    String falseLabel = element.has("labelFalse") ? element.get("labelFalse").asText() : "False";
                    answers.add(new Answer(trueLabel, correct));
                    answers.add(new Answer(falseLabel, !correct));
                }

                Question q = QuestionFactory.createQuestion(type, title, answers);
                q.setTimeLimit(timeLimit);
                questions.add(q);

            } catch (Exception e) {
                System.out.println("⚠ Skipped one invalid question: " + e.getMessage());
            }
        }
    }
}
