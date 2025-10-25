package nl.inholland.quizgame.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class UserRepository {

    public boolean validateUser(String username, String password) {
        try {
            File file = new File("src/main/resources/nl/inholland/quizgame/users.json");
            if (!file.exists()) {
                System.out.println("⚠ users.json not found!");
                return false;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode users = mapper.readTree(file);

            for (JsonNode user : users) {
                String name = user.get("username").asText();
                String pass = user.get("password").asText();

                if (name.equalsIgnoreCase(username) && pass.equals(password))
                    return true;
            }

        } catch (IOException e) {
            System.out.println("Error reading users.json: " + e.getMessage());
        }

        return false;
    }
}
