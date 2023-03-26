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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 2 objects in the system");
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
  public void testAddTodoWith1000ObjectsInSystem() throws Exception {

    // add 1000 todos
    for (int i = 0; i < 1000; i++) {
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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 1000 objects in the system");
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
        "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 1000 objects in the system");

    // delete a todo
    long startTimeForDelete = System.nanoTime();
    HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
    long endTimeForDelete = System.nanoTime();
    long durationForDelete = (endTimeForDelete - startTimeForDelete);
    System.out.println(
        "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 1000 objects in the system");

  }

  @Test
  @Order(3)
  public void testAddTodoWith2000ObjectsInSystem() throws Exception {

    // add 2000 todos
    for (int i = 0; i < 2000; i++) {
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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 2000 objects in the system");
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
        "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 2000 objects in the system");

    // delete a todo
    long startTimeForDelete = System.nanoTime();
    HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
    long endTimeForDelete = System.nanoTime();
    long durationForDelete = (endTimeForDelete - startTimeForDelete);
    System.out.println(
        "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 2000 objects in the system");
  }

  @Test
  @Order(4)
  public void testAddTodoWith3000ObjectsInSystem() throws Exception {

    // add 3000 todos
    for (int i = 0; i < 3000; i++) {
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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 3000 objects in the system");
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
        "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 3000 objects in the system");

    // delete a todo
    long startTimeForDelete = System.nanoTime();
    HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
    long endTimeForDelete = System.nanoTime();
    long durationForDelete = (endTimeForDelete - startTimeForDelete);
    System.out.println(
        "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 3000 objects in the system");
  }

  @Test
  @Order(5)
  public void testAddTodoWith4000ObjectsInSystem() throws Exception {

    // add 4000 todos
    for (int i = 0; i < 4000; i++) {
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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 4000 objects in the system");
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
        "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 4000 objects in the system");

    // delete a todo
    long startTimeForDelete = System.nanoTime();
    HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
    long endTimeForDelete = System.nanoTime();
    long durationForDelete = (endTimeForDelete - startTimeForDelete);
    System.out.println(
        "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 4000 objects in the system");
  }

  @Test
  @Order(6)
  public void testAddTodoWith5000ObjectsInSystem() throws Exception {

    // add 5000 todos
    for (int i = 0; i < 5000; i++) {
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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 5000 objects in the system");
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
        "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 5000 objects in the system");

    // delete a todo
    long startTimeForDelete = System.nanoTime();
    HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
    long endTimeForDelete = System.nanoTime();
    long durationForDelete = (endTimeForDelete - startTimeForDelete);
    System.out.println(
        "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 5000 objects in the system");
  }

  @Test
  @Order(7)
  public void testAddTodoWith10000ObjectsInSystem() throws Exception {
    // add 10000 todos
    for (int i = 0; i < 10000; i++) {
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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 10000 objects in the system");
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
        "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 10000 objects in the system");

    // delete a todo
    long startTimeForDelete = System.nanoTime();
    HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
    long endTimeForDelete = System.nanoTime();
    long durationForDelete = (endTimeForDelete - startTimeForDelete);
    System.out.println(
        "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 10000 objects in the system");
  }

  @Test
  @Order(8)
  public void testAddTodoWith100000ObjectsInSystem() throws Exception {
    // add 100000 todos
    for (int i = 0; i < 100000; i++) {
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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 100000 objects in the system");
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
        "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 100000 objects in the system");

    // delete a todo
    long startTimeForDelete = System.nanoTime();
    HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
    long endTimeForDelete = System.nanoTime();
    long durationForDelete = (endTimeForDelete - startTimeForDelete);
    System.out.println(
        "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 100000 objects in the system");
  }

  @Test
  @Order(9)
  public void testAddTodoWith1000000ObjectsInSystem() throws Exception {
    // add 1000000 todos
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
        .println("Time taken for add: " + (float) durationForAdd / 1000000 + " ms with 1000000 objects in the system");
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
        "Time taken for update: " + (float) durationForUpdate / 1000000 + " ms with 1000000 objects in the system");

    // delete a todo
    long startTimeForDelete = System.nanoTime();
    HttpRequest.newBuilder().uri(URI.create(BASE_URL + "todos/" + givenId)).DELETE().build();
    long endTimeForDelete = System.nanoTime();
    long durationForDelete = (endTimeForDelete - startTimeForDelete);
    System.out.println(
        "Time taken for delete: " + (float) durationForDelete / 1000000 + " ms with 1000000 objects in the system");
  }

}
