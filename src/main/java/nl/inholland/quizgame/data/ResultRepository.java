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

    public void saveResult(Result result) {
        File quizFile = GameManager.getInstance().getQuizFile();
        if (quizFile == null) {
            System.out.println(" Cannot save result: no quiz file loaded.");
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

            System.out.println(" Result saved successfully to " + resultsFile.getPath());

        } catch (IOException e) {
            System.out.println(" Error saving results: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(" Unexpected error while saving results: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Result> loadResults() {
        List<Result> results = new ArrayList<>();

        try {
            File quizFile = GameManager.getInstance().getQuizFile();
            if (quizFile == null) {
                System.out.println(" Cannot load results: no quiz file loaded.");
                return results;
            }

            String quizName = quizFile.getName().replace(".json", "");
            File resultsFile = new File("results/" + quizName + "-results.json");

            if (!resultsFile.exists()) {
                System.out.println("ℹ No results file found for this quiz.");
                return results;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(resultsFile);
            JsonNode resultsArray = root.get("results");

            if (resultsArray != null && resultsArray.isArray()) {
                for (JsonNode node : resultsArray) {
                    String playerName = node.get("playerName").asText();
                    int totalQuestions = node.get("totalQuestions").asInt();
                    int correctQuestions = node.get("correctQuestions").asInt();

                    results.add(new Result(playerName, totalQuestions, correctQuestions));
                }
            }

            System.out.println(" Loaded " + results.size() + " results from file.");

        } catch (IOException e) {
            System.out.println(" Error reading results file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error loading results: " + e.getMessage());
        }

        return results;
    }


}
