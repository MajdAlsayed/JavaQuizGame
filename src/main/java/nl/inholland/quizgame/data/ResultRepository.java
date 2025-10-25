package nl.inholland.quizgame.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.inholland.quizgame.model.GameManager;
import nl.inholland.quizgame.model.Result;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ResultRepository {

    // Save player result to JSON file
    public void saveResult(Result result) {
        File quizFile = GameManager.getInstance().getQuizFile();
        if (quizFile == null) {
            System.out.println("⚠ Cannot save result: no quiz file loaded.");
            return;
        }

        String quizName = quizFile.getName().replace(".json", "");
        File resultsFile = new File("results/" + quizName + "-results.json");

        try {
            if (!resultsFile.getParentFile().exists())
                resultsFile.getParentFile().mkdirs();

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root;
            ArrayNode resultsArray;

            if (resultsFile.exists()) {
                root = (ObjectNode) mapper.readTree(resultsFile);
                resultsArray = (ArrayNode) root.get("results");
            } else {
                root = mapper.createObjectNode();
                resultsArray = mapper.createArrayNode();
                root.put("quizId", quizName);
                root.put("name", "Quiz Results");
                root.set("results", resultsArray);
            }

            ObjectNode newResult = mapper.createObjectNode();
            newResult.put("playerName", result.getPlayerName());
            newResult.put("totalQuestions", result.getTotalQuestions());
            newResult.put("correctQuestions", result.getCorrectQuestions());
            newResult.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

            resultsArray.add(newResult);
            mapper.writerWithDefaultPrettyPrinter().writeValue(resultsFile, root);

            System.out.println("Result saved successfully to " + resultsFile.getPath());

        } catch (IOException e) {
            System.out.println("Error saving results: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error while saving results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load all saved results from JSON
    public List<Result> loadResults() {
        List<Result> results = new ArrayList<>();

        try {
            File resultsDir = new File("results");
            if (!resultsDir.exists() || !resultsDir.isDirectory()) {
                System.out.println("ℹ No results directory found.");
                return results;
            }

            ObjectMapper mapper = new ObjectMapper();

            for (File file : resultsDir.listFiles((dir, name) -> name.endsWith("-results.json"))) {
                JsonNode root = mapper.readTree(file);
                JsonNode resultsArray = root.get("results");

                if (resultsArray != null && resultsArray.isArray()) {
                    for (JsonNode node : resultsArray) {
                        String playerName = node.get("playerName").asText();
                        int totalQuestions = node.get("totalQuestions").asInt();
                        int correctQuestions = node.get("correctQuestions").asInt();
                        String date = node.get("date").asText();

                        results.add(new Result(playerName, totalQuestions, correctQuestions, date));
                    }
                }
            }

            System.out.println("📊 Loaded " + results.size() + " total results from all quizzes.");

        } catch (Exception e) {
            System.out.println("Error loading results: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

}
