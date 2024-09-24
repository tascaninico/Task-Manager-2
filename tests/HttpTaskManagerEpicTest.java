import org.junit.jupiter.api.*;
import java.lang.Integer;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.stream.Collectors;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerEpicTest extends BaseFieldsForTests {

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
    public void getEpic() throws IOException, InterruptedException{

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + String.valueOf(epic.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskFromJson = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic.getId(), taskFromJson.getId(), "Epics are not equal");
    }

    @Test
    public void getAllEpics() throws IOException, InterruptedException{

        Epic epic1 = new Epic("Plane building", "To build a plane");

        manager.addNewEpic(epic1);

        LocalDateTime investorsTime = LocalDateTime.of(2020, Month.DECEMBER, 1, 10, 0);
        Duration investorsDuration = Duration.ofMinutes(60 * 48);
        Subtask subtask3 = new Subtask("Investing", "Need to find investors who is ready to support the project", StatusOfTask.NEW,
                epic1.getId(), investorsTime, investorsDuration);

        manager.addNewSubtaskForEpic(subtask3);

        List<Integer> listOfId = manager.getEpicList().stream().map(Task::getId).collect(Collectors.toList());
        listOfId = listOfId.stream().sorted().collect(Collectors.toList());

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Epic> listOfEpicsFromServer = gson.fromJson(response.body(), new EpicListTypeToken().getType());
        List<Integer> listOfIdFromServer = listOfEpicsFromServer.stream().map(Epic::getId).collect(Collectors.toList());
        listOfIdFromServer = listOfIdFromServer.stream().sorted().collect(Collectors.toList());

        assertEquals(listOfId, listOfIdFromServer, "Lists are not equal");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Plane building", "To build a plane");

        LocalDateTime investorsTime = LocalDateTime.of(2020, Month.DECEMBER, 1, 10, 0);
        Duration investorsDuration = Duration.ofMinutes(60 * 48);
        Subtask subtask3 = new Subtask("Investing", "Need to find investors who is ready to support the project", StatusOfTask.NEW,
                epic1.getId(), investorsTime, investorsDuration);

        String epicJson0 = gson.toJson(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson0)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Incorrect code method");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {

        Epic epicUpdated = new Epic("Rocket building", "To build a rocket");

        epicUpdated.setId(epic.getId());

        String epicJson0 = gson.toJson(epicUpdated);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + String.valueOf(epic.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson0)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Incorrect code method");
        assertEquals(manager.getEpicByID(epic.getId()).getName(), epicUpdated.getName());
        assertEquals(manager.getEpicByID(epic.getId()).getDescription(), epicUpdated.getDescription());
    }
    @Test
    public void testGetSubtasksOfEpic() throws IOException, InterruptedException{

        LocalDateTime transportingTime = LocalDateTime.of(2024, Month.JULY, 12, 9, 30);
        Duration transportingDuration = Duration.ofSeconds(60 * 210);
        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic.getId(), transportingTime, transportingDuration);

        manager.addNewSubtaskForEpic(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + String.valueOf(epic.getId()) +"/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Subtask> listOfSubtaskFromServer = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());
        List<Integer> listOfIdFromServer = listOfSubtaskFromServer.stream().map(Subtask::getId).sorted().collect(Collectors.toList());
        List<Integer> listOfIdFromLocalManager = manager.getSubtaskList().stream().map(Subtask::getId).sorted().collect(Collectors.toList());

        assertEquals(listOfIdFromLocalManager, listOfIdFromServer, "Lists of subtasks aren't equal");
    }


    @Test
    public void testDeleteTask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + String.valueOf(epic.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Epic hasn't been deleted");
        Assertions.assertFalse(manager.getTaskList().contains(epic));
    }
}
