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

    // @AfterEach
    // // kill the process at the end of the test
    // void setdown() throws InterruptedException {
    // runTodoManagerRestApi.destroy();
    // Thread.sleep(1000);
    // }

    @Test
    public void testGetTodo() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))

                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        int count = 0;
        JsonNode todosNode = jsonNode.get("todos");
        for (JsonNode todoNode : todosNode) {
            count++;
        }
        System.out.println("Number of todos in the system: " + count);

    }

    @Test
    @Order(1)
    public void testAddTodoWith2ObjectsInSystem() throws Exception {

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println(
                        "Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 2 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 2 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 2 objects in the system");

    }

    @Test
    @Order(2)
    public void testAddTodoWith100000ObjectsInSystem() throws Exception {

        // add 10000 todos
        HttpClient httpClient = HttpClient.newHttpClient();
        for (int i = 0; i < 10000; i++) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();

            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .body(); // This line is optional - it retrieves the response body as a string

        }

        // counting number of todos in the system
        HttpRequest requestCount = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .build();
        HttpClient clientCount = HttpClient.newHttpClient();
        HttpResponse<String> responseCount = clientCount.send(requestCount, BodyHandlers.ofString());
        ObjectMapper objectMapperCount = new ObjectMapper();
        JsonNode jsonNodeCount = objectMapperCount.readTree(responseCount.body());
        int count = 0;
        JsonNode todosNode = jsonNodeCount.get("todos");
        for (JsonNode todoNode : todosNode) {
            count++;
        }
        System.out.println("Number of todos in the system: " + count);

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 100000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 100000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 100000 objects in the system");

    }

    @Test
    @Order(3)
    public void testAddTodoWith200000ObjectsInSystem() throws Exception {

        // add 20000 todos
        for (int i = 0; i < 200000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 200000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 200000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 200000 objects in the system");
    }

    @Test
    @Order(4)
    public void testAddTodoWith300000ObjectsInSystem() throws Exception {

        // add 30000 todos
        for (int i = 0; i < 300000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 300000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 300000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 300000 objects in the system");
    }

    @Test
    @Order(5)
    public void testAddTodoWith400000ObjectsInSystem() throws Exception {

        // add 40000 todos
        for (int i = 0; i < 400000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 400000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 400000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 400000 objects in the system");
    }

    @Test
    @Order(6)
    public void testAddTodoWith50000ObjectsInSystem() throws Exception {

        // add 50000 todos
        for (int i = 0; i < 500000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 500000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 500000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 500000 objects in the system");
    }

    @Test
    @Order(7)
    public void testAddTodoWith600000ObjectsInSystem() throws Exception {

        // add 60000 todos
        for (int i = 0; i < 600000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 600000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 600000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 600000 objects in the system");
    }

    @Test
    @Order(8)
    public void testAddTodoWith700000ObjectsInSystem() throws Exception {

        // add 60000 todos
        for (int i = 0; i < 700000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 700000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 700000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 700000 objects in the system");
    }

    @Test
    @Order(9)
    public void testAddTodoWith800000ObjectsInSystem() throws Exception {

        // add 60000 todos
        for (int i = 0; i < 800000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 800000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 800000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 800000 objects in the system");
    }

    @Test
    @Order(9)
    public void testAddTodoWith900000ObjectsInSystem() throws Exception {

        // add 60000 todos
        for (int i = 0; i < 900000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // parse number of objects
        HttpClient clientCount = HttpClient.newHttpClient();
        HttpRequest requestCount = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .build();
        HttpResponse<String> responseCount = clientCount.send(requestCount, BodyHandlers.ofString());
        ObjectMapper objectMapperCount = new ObjectMapper();
        JsonNode jsonNodeCount = objectMapperCount.readTree(responseCount.body());
        int count = jsonNodeCount.size();
        System.out.println("Number of objects in the system: " + count);

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 900000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 900000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 900000 objects in the system");
    }

    @Test
    @Order(10)
    public void testAddTodoWith1000000ObjectsInSystem() throws Exception {

        // add 60000 todos
        for (int i = 0; i < 1000000; i++) {
            HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "todos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                    .build();
        }

        // add a todo
        long startTimeForAdd = System.nanoTime();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": false, \"description\": \"Test description\"}"))
                .build();
        long endTimeForAdd = System.nanoTime();
        long durationForAdd = (endTimeForAdd - startTimeForAdd);
        System.out
                .println("Time taken for add: " + (float) durationForAdd / 1000000
                        + " ms with 1000000 objects in the system");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        givenId = jsonNode.get("id").asInt();

        // update a todo
        long startTimeForUpdate = System.nanoTime();
        HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "todos/" + givenId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"title\": \"Test1\", \"doneStatus\": true, \"description\": \"Test description\"}"))
                .build();
        long endTimeForUpdate = System.nanoTime();
        long durationForUpdate = (endTimeForUpdate - startTimeForUpdate);
        System.out.println(
                "Time taken for update: " + (float) durationForUpdate / 1000000
                        + " ms with 1000000 objects in the system");

        // delete a todo
        long startTimeForDelete = System.nanoTime();
        HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
        long endTimeForDelete = System.nanoTime();
        long durationForDelete = (endTimeForDelete - startTimeForDelete);
        System.out.println(
                "Time taken for delete: " + (float) durationForDelete / 1000000
                        + " ms with 1000000 objects in the system");
    }

}
