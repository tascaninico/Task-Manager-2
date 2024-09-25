import org.junit.jupiter.api.*;
import java.lang.Integer;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.stream.Collectors;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTasksTest extends BaseFieldsForTests {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        httpTaskServer.start();

    }

    @Test
    public void getTask() throws IOException, InterruptedException{
        LocalDateTime catFeedingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 0);
        Duration catFeedingDuration = Duration.ofSeconds(120);
        Task task0 = new Task("Cat feeding", "To fill a cat's dish",
                StatusOfTask.NEW, catFeedingTime, catFeedingDuration);
        manager.addNewTask(task0);

        URI url = URI.create("http://localhost:8080/tasks/" + String.valueOf(task0.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskFromJson = gson.fromJson(response.body(), Task.class);
        assertEquals(task0.getId(), taskFromJson.getId(), "Tasks are not equal");
    }

    @Test
    public void getAllTask() throws IOException, InterruptedException{
        LocalDateTime catFeedingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 0);
        Duration catFeedingDuration = Duration.ofSeconds(120);
        Task task0 = new Task("Cat feeding", "To fill a cat's dish",
                StatusOfTask.NEW, catFeedingTime, catFeedingDuration);
        manager.addNewTask(task0);

        LocalDateTime borshCookingTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 0);
        Duration borchCookingDuration = Duration.ofSeconds(60*60*2);
        Task task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh and cook a borsh",
                StatusOfTask.NEW, borshCookingTime, borchCookingDuration);
        manager.addNewTask(task1);

        List<Integer> listOfId = manager.getTaskList().stream().map(Task::getId).collect(Collectors.toList());
        listOfId = listOfId.stream().sorted().collect(Collectors.toList());

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> listOfTasksFromServer = gson.fromJson(response.body(), new TaskListTypeToken().getType());
        List<Integer> listOfIdFromServer = listOfTasksFromServer.stream().map(Task::getId).collect(Collectors.toList());
        listOfIdFromServer = listOfIdFromServer.stream().sorted().collect(Collectors.toList());

        assertEquals(listOfId, listOfIdFromServer, "Lists are not equal");
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {

        LocalDateTime catFeedingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 0);
        Duration catFeedingDuration = Duration.ofSeconds(120);
        Task task0 = new Task("Cat feeding", "To fill a cat's dish",
                StatusOfTask.NEW, catFeedingTime, catFeedingDuration);

        String taskJson0 = gson.toJson(task0);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson0)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Incorrect code method");


        LocalDateTime borshCookingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 1);
        Duration borchCookingDuration = Duration.ofSeconds(60*60*2);
        Task task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh and cook a borsh",
                StatusOfTask.NEW, borshCookingTime, borchCookingDuration);

        String taskJson1 = gson.toJson(task1);

        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Intersection with another task hasn't occurred");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {

        LocalDateTime workingOutTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 30);
        Duration workingOutDuration = Duration.ofSeconds(60 * 90);
        Task task3 = new Task("Working out", "To do some exercises",
                StatusOfTask.NEW, workingOutDuration);
        manager.addNewTask(task3);

        Task task3Updated = new Task("Working out", "To do some exercises",
                StatusOfTask.NEW, workingOutTime, workingOutDuration);

        task3Updated.setId(task3.getId());

        String taskJson3 = gson.toJson(task3Updated);

        URI url = URI.create("http://localhost:8080/tasks/" + String.valueOf(task3.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson3)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Status codes aren't equal");
        Assertions.assertTrue(manager.getTaskByID(task3.getId()).getStartTime().isPresent());
        Assertions.assertTrue(manager.getTaskByID(task3.getId()).getEndTime().isPresent());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {

        LocalDateTime catFeedingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 0);
        Duration catFeedingDuration = Duration.ofSeconds(120);
        Task task0 = new Task("Cat feeding", "To fill a cat's dish",
                StatusOfTask.NEW, catFeedingTime, catFeedingDuration);
        manager.addNewTask(task0);


        LocalDateTime borshCookingTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 0);
        Duration borchCookingDuration = Duration.ofSeconds(60*60*2);
        Task task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh and cook a borsh",
                StatusOfTask.NEW, borshCookingTime, borchCookingDuration);
        manager.addNewTask(task1);


        URI url = URI.create("http://localhost:8080/tasks/" + String.valueOf(task1.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Task hasn't been deleted");
        Assertions.assertFalse(manager.getTaskList().contains(task1));
    }
}
