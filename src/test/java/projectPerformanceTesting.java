import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class projectPerformanceTesting {
    private final String BASE_URL = "http://localhost:4567/";
    private Process runTodoManagerRestApi;

    @BeforeEach
    public void setUp() throws InterruptedException {
        try {
            runTodoManagerRestApi = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Make sure application is running
        System.out.println("Starting tests in...\n");
        for (int i = 3; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(100);
        }
    }

    @AfterEach
    // Todo: Delete all todos, projects and categories before the next test
    void setdown() throws InterruptedException {
        runTodoManagerRestApi.destroy();
        Thread.sleep(1000);
    }
    @Test
    void testProjectCreate() throws Exception {
        for (int i = 0; i <= 20; i++) {
            // Measure time to create
            long startTimeCreate = System.nanoTime();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "projects"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test\", \"completed\": true, \"active\": false, \"description\": \"Testing POST request for project\"}"))
                    .build();
            long endTimeCreate = System.nanoTime();
            long durationCreate = endTimeCreate - startTimeCreate;
            int numberOfProjects = i*1000;
            System.out.println("Time to create Project when " + numberOfProjects + " instances is: " + durationCreate);

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            int createdId = jsonNode.get("id").asInt();

            // Measure time to update
            long startTimeUpdate = System.nanoTime();
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "projects/" + createdId))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test\", \"completed\": true, \"active\": false, \"description\": \"Testing POST request for project\"}"))
                    .build();
            long endTimeUpdate = System.nanoTime();
            long durationUpdate = endTimeUpdate - startTimeUpdate;
            System.out.println("Time to update Project when " + numberOfProjects + " instances is: " + durationUpdate);

            // Measure time to delete
            long startTimeDelete = System.nanoTime();
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "projects/" + createdId))
                    .DELETE()
                    .build();
            long endTimeDelete = System.nanoTime();
            long durationDelete = endTimeDelete - startTimeDelete;
            System.out.println("Time to delete Project when " + numberOfProjects + " instances is: " + durationDelete);

            for (int j = 0; j < 1000; j++) {
                HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "projects"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"title\": \"Test\", \"completed\": true, \"active\": false, \"description\": \"Testing POST request for project\"}"))
                        .build();
            }
        }
    }
}
