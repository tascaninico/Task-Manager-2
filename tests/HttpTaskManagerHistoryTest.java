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

public class HttpTaskManagerHistoryTest extends BaseFieldsForTests {

    @BeforeEach
    @Override
    public void setUp(){

        LocalDateTime catFeedingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 0);
        Duration catFeedingDuration = Duration.ofSeconds(120);
        Task task0 = new Task("Cat feeding", "To fill a cat's dish",
                StatusOfTask.NEW, catFeedingTime, catFeedingDuration);
        manager.addNewTask(task0);

        Epic epic0 = new Epic("Moving on", "Changing residence");
        manager.addNewEpic(epic0);

        LocalDateTime packingTime = LocalDateTime.of(2024, Month.JULY, 12, 7, 0);
        Duration packingDuration = Duration.ofSeconds(60 * 150);
        Subtask subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW,
                epic0.getId(), packingTime, packingDuration);
        manager.addNewSubtaskForEpic(subtask0);

        LocalDateTime unpackingTime = LocalDateTime.of(2024, Month.JULY, 12, 13, 0);
        Duration unpackingDuration = Duration.ofSeconds(60 * 240);
        Subtask subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW,
                epic0.getId(), unpackingTime, unpackingDuration);
        manager.addNewSubtaskForEpic(subtask2);

        manager.getTaskByID(1);
        manager.getSubtaskByID(3);
        manager.getEpicByID(2);
        manager.getSubtaskByID(4);

        httpTaskServer.start();
    }

    @Test
    public void historyTest() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> historyFromServerTasks = gson.fromJson(response.body(), new TaskListTypeToken().getType());
        List<Integer> historyFromServerId = historyFromServerTasks.stream().map(Task::getId).collect(Collectors.toList());

        List<Integer> historyFromLocalManager = manager.getHistory().stream().map(Task::getId).collect(Collectors.toList());

        assertEquals(200, response.statusCode(), "Status code isn't correct");
        assertEquals(historyFromServerId, historyFromLocalManager, "Tasks are not equal");
    }

}
