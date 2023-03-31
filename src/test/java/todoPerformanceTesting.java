import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(OrderAnnotation.class)
public class todoPerformanceTesting {
    private static final String BASE_URL = "http://localhost:4567/";
    private Process runTodoManagerRestApi;
    private static int givenId = 0;

    @BeforeEach
    public void setUp() throws InterruptedException {
        try {
            runTodoManagerRestApi = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Make sure application is running
        System.out.println("Starting tests in...");
        for (int i = 3; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(100);
        }
    }

    @AfterEach
    // kill the process at the end of the test
    void setdown() throws InterruptedException {
        runTodoManagerRestApi.destroy();
        Thread.sleep(1000);
    }

    @Test
    @Order(1)
    public void testTransactionTimeTodo() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < 11; i++) {
            // counting number of todos in the system
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            JsonNode jsonNode = objectMapper.readTree(response.body());
            JsonNode todosNode = jsonNode.get("todos");
            int count = 0;
            for (JsonNode todoNode : todosNode) {
                count++;
            }
            System.out.println("Number of todos currently in the system: " + count);

            // add a todo
            long startTimeForAdd = System.nanoTime();
            request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
            response = client.send(request, BodyHandlers.ofString());
            long endTimeForAdd = System.nanoTime();
            long durationForAdd = (endTimeForAdd - startTimeForAdd);

            System.out
                    .println("Time taken for add: " + (float) durationForAdd / 1000000
                            + " ms with " + count + " objects in the system");

            jsonNode = objectMapper.readTree(response.body());
            givenId = jsonNode.get("id").asInt();

            // update a todo
            long startTimeForUpdate = System.nanoTime();
            request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos/" + givenId))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                    .build();
            client.send(request, BodyHandlers.ofString());
            long endTimeForUpdate = System.nanoTime();
            long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);

            System.out.println("Time taken for update: " + (float) durationForUpdate / 1000000
                    + " ms with " + count + " objects in the system");

            // delete a todo
            long startTimeForDelete = System.nanoTime();
            request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos/" + givenId))
                    .DELETE()
                    .build();

            client.send(request, BodyHandlers.ofString());
            long endTimeForDelete = System.nanoTime();
            long durationForDelete = (endTimeForDelete - startTimeForDelete);

            System.out.println("Time taken for delete: " + (float) durationForDelete / 1000000
                    + " ms with " + count + " objects in the system");

            // add 100000 todos to the system
            for (int j = 0; j < 10000; j++) {
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "todos"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                        .build();

                client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            }

        }
    }
}