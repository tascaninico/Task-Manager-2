
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

public class HttpTaskManagerSubtasksTest extends BaseFieldsForTests {

    Epic epic = new Epic("Moving on", "Changing residence");

    Subtask subtask0;

    @BeforeEach
    public void setUp() {
        super.setUp();
        manager.addNewEpic(epic);
        subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW,
                epic.getId(), LocalDateTime.of(2024, Month.JULY, 12, 7, 0), Duration.ofSeconds(60 * 150));
        manager.addNewSubtaskForEpic(subtask0);
        httpTaskServer.start();
    }


    @Test
    public void getSubtask() throws IOException, InterruptedException{

        URI url = URI.create("http://localhost:8080/subtasks/" + String.valueOf(subtask0.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskFromJson = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask0.getId(), taskFromJson.getId(), "Subtasks are not equal");
    }

    @Test
    public void getAllSubtasks() throws IOException, InterruptedException{

        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic.getId(), LocalDateTime.of(2024, Month.JULY, 12, 10,0 ), Duration.ofSeconds(60 * 210));

        manager.addNewSubtaskForEpic(subtask1);

        List<Integer> listOfId = manager.getSubtaskList().stream().map(Subtask::getId).collect(Collectors.toList());
        listOfId = listOfId.stream().sorted().collect(Collectors.toList());

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Subtask> listOfSubtasksFromServer = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());
        List<Integer> listOfIdFromServer = listOfSubtasksFromServer.stream().map(Subtask::getId).collect(Collectors.toList());
        listOfIdFromServer = listOfIdFromServer.stream().sorted().collect(Collectors.toList());

        assertEquals(listOfId, listOfIdFromServer, "Lists are not equal");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {

        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic.getId(), LocalDateTime.of(2024, Month.JULY, 12, 9, 31), Duration.ofSeconds(60 * 210));

        String subtaskJson1 = gson.toJson(subtask1);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson1)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Incorrect code method");
        assertEquals(2, manager.getSubtaskList().size());


        Subtask subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW,
                epic.getId(), LocalDateTime.of(2024, Month.JULY, 12, 10, 0), Duration.ofSeconds(60 * 240));

        String taskJson1 = gson.toJson(subtask2);

        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Intersection with another task hasn't occurred");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {

        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic.getId(), LocalDateTime.of(2024, Month.JULY, 12, 9, 31), Duration.ofSeconds(60 * 210));

        manager.addNewSubtaskForEpic(subtask1);

        Subtask subtask1Updated = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic.getId(), LocalDateTime.of(2024, Month.JULY, 12, 9, 40), Duration.ofSeconds(60 * 180));

        subtask1Updated.setId(subtask1.getId());

        String subtaskJson1 = gson.toJson(subtask1Updated);

        URI url = URI.create("http://localhost:8080/subtasks/" + String.valueOf(subtask1.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson1)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Subtask hasn't been updated");
        assertEquals(manager.getSubtaskByID(subtask1.getId()).getStartTime().get(), LocalDateTime.of(2024, Month.JULY, 12, 9, 40));
        assertEquals(manager.getSubtaskByID(subtask1.getId()).getDuration(), Duration.ofSeconds(60 * 180));
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {

        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic.getId(), LocalDateTime.of(2024, Month.JULY, 12, 10,0 ), Duration.ofSeconds(60 * 210));

        manager.addNewSubtaskForEpic(subtask1);

        URI url = URI.create("http://localhost:8080/subtasks/" + String.valueOf(subtask1.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Task hasn't been deleted");
        Assertions.assertFalse(manager.getTaskList().contains(subtask1));
    }
}
